package com.example.projectmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.dataModels.Project
import com.example.projectmanager.R

class RecyclerViewProjectsAdapter(
    private val projectList: List<Project>,
    private val onItemClick: (Project) -> Unit,
) : RecyclerView.Adapter<RecyclerViewProjectsAdapter.ViewHolder>(){

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

         val projectName: TextView = itemView.findViewById(R.id.name)
         val pendingTasks: TextView = itemView.findViewById(R.id.pending_tasks_count)
         val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val project = projectList[position]
        holder.projectName.text = project.name_project

        val pendingCount = project.getPendingTasksCount()
        if (pendingCount == 1){
            holder.pendingTasks.text = "$pendingCount tarea pendiente"
        }else{
            holder.pendingTasks.text = "$pendingCount tareas pendientes"
        }

        holder.cardView.visibility = View.VISIBLE


        holder.cardView.setOnClickListener {
            onItemClick(project)
        }
    }

    override fun getItemCount(): Int {
        return projectList.size
    }
}