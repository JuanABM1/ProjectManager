package com.example.projectmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FullTaskAdapter(
    private val taskList: List<Task>,
    private val onItemClick: (Task) -> Unit
) :
    RecyclerView.Adapter<FullTaskAdapter.FullTaskViewHolder>() {

    class FullTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.taskName)
        val taskDescription: TextView = view.findViewById(R.id.taskDescription)
        val taskState: TextView = view.findViewById(R.id.taskState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.full_task_information_layout, parent, false)
        return FullTaskViewHolder(view)
    }


    override fun onBindViewHolder(holder: FullTaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.nombre_tarea
        holder.taskDescription.text = task.descripcion
        holder.taskState.text = task.estado

        holder.itemView.setOnClickListener{
            onItemClick(task)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
