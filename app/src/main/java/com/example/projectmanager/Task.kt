package com.example.projectmanager

import java.io.Serializable
import java.time.LocalDate

class Task (
    val id_tarea: Int,
    val nombre_tarea: String,
    val fecha_inicio: String,
    val fecha_final: String,
    val descripcion: String,
    val estado: String
) : Serializable