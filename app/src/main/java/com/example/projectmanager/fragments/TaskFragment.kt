package com.example.projectmanager.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.dataModels.Project
import com.example.projectmanager.R
import com.example.projectmanager.dataModels.Task
import com.example.projectmanager.dataModels.User
import com.example.projectmanager.adapters.TaskAdapter
import java.text.SimpleDateFormat
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {

    private lateinit var recyclerViewThisWeek: RecyclerView
    private lateinit var recyclerViewNextWeek: RecyclerView
    private lateinit var recyclerViewLater: RecyclerView


    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = it.getSerializable("user") as User
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categorizedTasks = user.projects?.let { categorizeTasks(it) }

        // Configurar RecyclerView para "Esta semana"
        recyclerViewThisWeek = view.findViewById(R.id.recyclerViewThisWeek)
        recyclerViewThisWeek.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewThisWeek.adapter = TaskAdapter(
            categorizedTasks?.get("thisWeek") ?: emptyList())

        // Configurar RecyclerView para "Próxima semana"
        recyclerViewNextWeek = view.findViewById(R.id.recyclerViewNextWeek)
        recyclerViewNextWeek.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewNextWeek.adapter = TaskAdapter(
            categorizedTasks?.get("nextWeek") ?: emptyList())

        // Configurar RecyclerView para "Más tarde"
        recyclerViewLater = view.findViewById(R.id.recyclerViewLater)
        recyclerViewLater.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewLater.adapter = TaskAdapter(
            categorizedTasks?.get("later") ?: emptyList())
    }

    // Función para categorizar las tareas por fecha
    private fun categorizeTasks(projects: List<Project>): Map<String, List<Task>> {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        val tasksThisWeek = mutableListOf<Task>()
        val tasksNextWeek = mutableListOf<Task>()
        val tasksLater = mutableListOf<Task>()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Recorremos los proyectos y sus tareas
        for (project in projects) {
            for (task in project.tasks) {
                val taskDate = dateFormat.parse(task.fecha_final)

                val taskCalendar = Calendar.getInstance()
                taskCalendar.time = taskDate

                val taskWeek = taskCalendar.get(Calendar.WEEK_OF_YEAR)
                val taskYear = taskCalendar.get(Calendar.YEAR)

                when {
                    taskYear == currentYear && taskWeek == currentWeek -> tasksThisWeek.add(task)
                    taskYear == currentYear && taskWeek == currentWeek + 1 -> tasksNextWeek.add(task)
                    taskDate.after(currentDate) -> tasksLater.add(task)
                }
            }
        }

        return mapOf(
            "thisWeek" to tasksThisWeek,
            "nextWeek" to tasksNextWeek,
            "later" to tasksLater
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) = TaskFragment().apply {
            arguments = Bundle().apply {
                putSerializable("user", user)
            }
        }
    }
}
