package com.example.projectmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewProjectsAdapter(
    private val projectList: List<Project>,
    private val onItemClick: (Project) -> Unit, // callbacks para manejar los clicks
    private val onSpecialItemClick: () -> Unit
) : RecyclerView.Adapter<RecyclerViewProjectsAdapter.ViewHolder>(){

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

         val projectName: TextView = itemView.findViewById(R.id.name)
         val pendingTasks: TextView = itemView.findViewById(R.id.pending_tasks_count)
         val cardView: CardView = itemView.findViewById(R.id.cardView)
         val specialItemView: CardView = itemView.findViewById(R.id.special_item)  // Vista especial
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Verificar si estamos en el ítem especial
        if (position == projectList.size) {
            // Mostrar el ítem especial (sumar más y texto "Crear")
            holder.specialItemView.visibility = View.VISIBLE
            holder.cardView.visibility = View.GONE

            // Acción cuando se hace clic en el ítem especial
            holder.specialItemView.setOnClickListener {
                onSpecialItemClick() // Llama al callback para el ítem especial
            }
        } else {
            // Para los ítems normales
            val project = projectList[position]
            holder.projectName.text = project.name_project

            val pendingCount = project.getPendingTasksCount()
            if (pendingCount == 1){
                holder.pendingTasks.text = "$pendingCount tarea pendiente"
            }else{
                holder.pendingTasks.text = "$pendingCount tareas pendientes"
            }


            holder.specialItemView.visibility = View.GONE
            holder.cardView.visibility = View.VISIBLE

            // Establecer el clic en los ítems normales
            holder.cardView.setOnClickListener {
                onItemClick(project) // Llama al callback para los ítems normales
            }
        }
    }

    override fun getItemCount(): Int {
        return projectList.size + 1
    }
}