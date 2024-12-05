package com.example.projectmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {

    private lateinit var recyclerViewThisWeek: RecyclerView
    private lateinit var recyclerViewNextWeek: RecyclerView
    private lateinit var recyclerViewLater: RecyclerView

    // Recibir el objeto User en los argumentos del fragmento
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener el objeto User de los argumentos
        arguments?.let {
            user = it.getSerializable("user") as User
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Categorizar las tareas
        val categorizedTasks = user.projects?.let { categorizeTasks(it) }

        // Configurar RecyclerView para "Esta semana"
        recyclerViewThisWeek = view.findViewById(R.id.recyclerViewThisWeek)
        recyclerViewThisWeek.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewThisWeek.adapter = TaskAdapter(
            categorizedTasks?.get("thisWeek") ?: emptyList()) { task ->
            // Acción al hacer clic en una tarea
        }

        // Configurar RecyclerView para "Próxima semana"
        recyclerViewNextWeek = view.findViewById(R.id.recyclerViewNextWeek)
        recyclerViewNextWeek.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewNextWeek.adapter = TaskAdapter(
            categorizedTasks?.get("nextWeek") ?: emptyList()) { task ->
            // Acción al hacer clic en una tarea
        }

        // Configurar RecyclerView para "Más tarde"
        recyclerViewLater = view.findViewById(R.id.recyclerViewLater)
        recyclerViewLater.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewLater.adapter = TaskAdapter(
            categorizedTasks?.get("later") ?: emptyList()) { task ->
            // Acción al hacer clic en una tarea
        }
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

        // Formato para comparar fechas
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
