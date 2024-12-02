package com.example.projectmanager

import java.io.Serializable

class User (
    val id: Int,
    val nombre: String,
    val usuario: String,
    val password: String,
    val rol: String,
    val projects: List<Project>? = null
) : Serializable