package com.example.projectmanager

import java.io.Serializable

class Task (
    val id_tarea: Int,
    val nombre_tarea: String,
    val horas_estimadas: Int,
    val horas_trabajadas: Int,
    val estado: String
) : Serializable