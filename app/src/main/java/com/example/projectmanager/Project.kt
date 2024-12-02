package com.example.projectmanager

import java.io.Serializable

class Project (

    val id_project: Int,
    val name_project: String,
    val tasks: List<Task>,
    val invitations: List<Invitation>

) : Serializable