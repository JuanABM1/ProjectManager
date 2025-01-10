package com.example.projectmanager.dataModels

import java.io.Serializable

class Task (
    val id_tarea: Int,
    val nombre_tarea: String,
    val fecha_final: String,
    val descripcion: String,
    var estado: String
) : Serializable