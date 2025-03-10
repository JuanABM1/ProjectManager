package com.example.projectmanager.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.DataManager
import com.example.projectmanager.dataModels.Project
import com.example.projectmanager.R
import com.example.projectmanager.dataModels.Task
import com.example.projectmanager.dataModels.User
import com.example.projectmanager.activities.ProjectInformation
import com.example.projectmanager.adapters.RecyclerViewProjectsAdapter
import com.example.projectmanager.adapters.TaskListAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var user: User
    private lateinit var allUsers: List<User>
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectList: ArrayList<Project>
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("user") as User
            allUsers = it.getSerializable("users") as ArrayList<User>
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val taskChanged = result.data?.getBooleanExtra("taskChanged", false) ?: false
                if (taskChanged) {
                    loadProjects()
                    updateTasks()
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        loadProjects() // Recargar proyectos cada vez que se regrese a esta pantalla
        updateTasks() // Si es necesario, actualiza las tareas también
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateTasks() {
        val tasksDueThisWeek = getTasksDueThisWeek()
        taskListAdapter.updateTasks(tasksDueThisWeek)
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
        ) { project ->
            val intent = Intent(requireContext(), ProjectInformation::class.java)
            val bundle = Bundle()
            bundle.putSerializable("project", project)
            bundle.putSerializable("allUsers", allUsers as Serializable)
            intent.putExtra("userId", user.id)
            intent.putExtras(bundle)
            resultLauncher.launch(intent)
        }
        recyclerView.adapter = recyclerViewProjectsAdapter

        taskRecyclerView = view.findViewById(R.id.recyclerViewTasks)
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tasksDueThisWeek = getTasksDueThisWeek()
        taskListAdapter = TaskListAdapter(tasksDueThisWeek)
        taskRecyclerView.adapter = taskListAdapter
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadProjects() {
        try {
            val users = DataManager.loadUsers(requireContext())

            user.projects = users.find { it.id == user.id }?.projects ?: emptyList()

            projectList.clear()
            projectList.addAll(user.projects!!)

            recyclerView.adapter?.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al cargar los proyectos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun getTasksDueThisWeek(): List<Task> {
        val tasksDueThisWeek = mutableListOf<Task>()
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time


        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysUntilEndOfWeek = Calendar.SATURDAY - dayOfWeek
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilEndOfWeek)
        val endOfWeek = calendar.time

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
        fun newInstance(user: User, allUsers : ArrayList<User>) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("user", user)
                    putSerializable("users", allUsers)
                }
            }
    }
}
