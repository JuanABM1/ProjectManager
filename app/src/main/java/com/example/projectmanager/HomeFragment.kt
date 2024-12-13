package com.example.projectmanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var user: User
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectList: ArrayList<Project>
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("user") as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        textViewName.text = user.nombre

        recyclerView = view.findViewById(R.id.recyclerViewProjects)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        projectList = ArrayList(user.projects)

        val recyclerViewProjectsAdapter = RecyclerViewProjectsAdapter(projectList,
            { project ->
                val intent = Intent(requireContext(), ProjectInformation::class.java)
                val bundle = Bundle()
                bundle.putSerializable("project", project)
                intent.putExtras(bundle)
                startActivity(intent)
            },
            {
                Toast.makeText(requireContext(), "Clic en: crear", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = recyclerViewProjectsAdapter

        taskRecyclerView = view.findViewById(R.id.recyclerViewTasks)
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tasksDueThisWeek = getTasksDueThisWeek()
        taskListAdapter = TaskListAdapter(tasksDueThisWeek) { task ->
            Toast.makeText(requireContext(), "click en tareas", Toast.LENGTH_SHORT).show()
        }
        taskRecyclerView.adapter = taskListAdapter
    }

    private fun getTasksDueThisWeek(): List<Task> {
        val tasksDueThisWeek = mutableListOf<Task>()
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        // Calcular el final de la semana
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysUntilEndOfWeek = Calendar.SATURDAY - dayOfWeek
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilEndOfWeek)
        val endOfWeek = calendar.time

        // Formateador para convertir fechas
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        user.projects?.forEach { project ->
            project.tasks.forEach { task ->
                val taskEndDate = formatter.parse(task.fecha_final)
                if (taskEndDate != null && taskEndDate >= currentDate && taskEndDate <= endOfWeek) {
                    tasksDueThisWeek.add(task)
                }
            }
        }
        return tasksDueThisWeek
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("user", user)
                }
            }
    }
}
