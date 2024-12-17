package com.example.projectmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ProjectInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        val textViewProjectName = findViewById<TextView>(R.id.ProjectName)
        val imageViewBack = findViewById<ImageView>(R.id.backButton)
        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val recyclerViewInvitations = findViewById<RecyclerView>(R.id.recyclerViewInvitations)
        val inviteButton = findViewById<Button>(R.id.buttonInvitate)
        val createButton = findViewById<Button>(R.id.ButtonCreateTask)
        val deleteButton = findViewById<Button>(R.id.buttonDelete)

        createButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        inviteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        deleteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

        deleteButton.setOnClickListener{
            deleteProject()
        }

        imageViewBack.setOnClickListener {
            finish()
        }

        createButton.setOnClickListener{
            Toast.makeText(this, "No se ha implementado la funcion de crear tareas", Toast.LENGTH_SHORT).show()
        }

        val bundle = intent.extras
        val project = bundle!!.getSerializable("project") as Project
        val allUsers = bundle.getSerializable("allUsers") as List<User> // Lista completa de usuarios


        textViewProjectName.text = project.name_project

        // Configurar RecyclerView de tareas
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = FullTaskAdapter(project.tasks) { task ->
            Toast.makeText(this, "Tarea seleccionada: ${task.nombre_tarea}", Toast.LENGTH_SHORT).show()
        }

        // Configurar RecyclerView de invitaciones
        recyclerViewInvitations.layoutManager = LinearLayoutManager(this)
        recyclerViewInvitations.adapter = InvitationAdapter(project.invitations, allUsers)

        // Configurar el botón de invitar personas
        inviteButton.setOnClickListener {
            Toast.makeText(this, "Función para invitar aún no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProject() {
        try {
            val file = File("/data/data/com.example.projectmanager/files/json/data.json")
            if (!file.exists()) {
                Toast.makeText(this, "El archivo de datos no existe", Toast.LENGTH_SHORT).show()
                return
            }

            val jsonString = file.readText()
            val users: MutableList<User> = Gson().fromJson(jsonString, object : TypeToken<MutableList<User>>() {}.type)


            val project = intent.extras?.getSerializable("project") as Project
            val userId = intent.extras?.getInt("userId") ?: return
            val user = users.find { it.id == userId }



            // Eliminar el proyecto del usuario
            user!!.projects = user.projects?.filter { it.id_project != project.id_project } ?: emptyList()

            // Guardar el archivo JSON actualizado
            val updatedJsonString = Gson().toJson(users)
            file.writeText(updatedJsonString)


            // Devolver un resultado y cerrar la actividad
            setResult(Activity.RESULT_OK)
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al eliminar el proyecto: ${e.message}", Toast.LENGTH_LONG)
                .show()

        }
    }
}
