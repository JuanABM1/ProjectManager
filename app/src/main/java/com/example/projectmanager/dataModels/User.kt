package com.example.projectmanager.dataModels

import java.io.Serializable

class User (
    val id: Int,
    val nombre: String,
    val usuario: String,
    val password: String,
    var projects: List<Project>? = null
) : Serializable