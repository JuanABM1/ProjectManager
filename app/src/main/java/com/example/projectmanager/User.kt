package com.example.projectmanager

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class User (
    val id: Int,
    val nombre: String,
    val usuario: String,
    val password: String,
    val rol: String,
    val tareas_asignadas: List<Tarea>? = null
) : Serializable