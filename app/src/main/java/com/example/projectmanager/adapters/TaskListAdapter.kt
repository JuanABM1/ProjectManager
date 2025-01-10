package com.example.projectmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.dataModels.Task

class TaskListAdapter(
    private var tasks: List<Task>
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.taskName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.nombre_tarea
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }
}
