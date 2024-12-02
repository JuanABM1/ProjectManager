package com.example.projectmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewProjectsAdapter(private val projectList: List<Project>) :
    RecyclerView.Adapter<RecyclerViewProjectsAdapter.ViewHolder>(){

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val projectName: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projectList[position]
        holder.projectName.text = project.name_project
    }

    override fun getItemCount(): Int {
        return projectList.size
    }
}