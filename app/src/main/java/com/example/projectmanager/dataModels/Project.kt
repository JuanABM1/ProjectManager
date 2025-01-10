package com.example.projectmanager.dataModels

import java.io.Serializable

class Project (

    val id_project: Int,
    val name_project: String,
    val tasks: List<Task>,

) : Serializable {

    fun getPendingTasksCount(): Int {
        return tasks.count() {it.estado == "pendiente"}
    }
}

