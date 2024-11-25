package com.example.projectmanager

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Tarea (
    val id_tarea: Int,
    val nombre_tarea: String,
    val horas_estimadas: Int,
    val horas_trabajadas: Int
) : Serializable