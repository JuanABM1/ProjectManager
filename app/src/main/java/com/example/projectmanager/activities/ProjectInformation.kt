package com.example.projectmanager.activities

import com.example.projectmanager.adapters.FullTaskAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.DataManager
import com.example.projectmanager.DataManager.encriptarJSON
import com.example.projectmanager.dataModels.Project
import com.example.projectmanager.R
import com.example.projectmanager.dataModels.Task
import com.example.projectmanager.dataModels.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ProjectInformation : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        val textViewProjectName = findViewById<TextView>(R.id.ProjectName)
        val imageViewBack = findViewById<ImageView>(R.id.backButton)
        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val deleteButton = findViewById<Button>(R.id.buttonDelete)

        deleteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

        deleteButton.setOnClickListener{
            deleteProject()
        }

        imageViewBack.setOnClickListener {
            finish()
        }

        val bundle = intent.extras
        val project = bundle!!.getSerializable("project") as Project

        textViewProjectName.text = project.name_project

        // Configurar RecyclerView de tareas
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = FullTaskAdapter(project.tasks.toMutableList()) { updatedTask ->
            updateTaskInJson(updatedTask)
            recyclerViewTasks.adapter?.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskInJson(updatedTask: Task) {
        try {
            // Cargar los usuarios desde el archivo
            val users = DataManager.loadUsers(this)

            val userId = intent.extras?.getInt("userId") ?: return
            val project = intent.extras?.getSerializable("project") as Project
            val user = users.find { it.id == userId }

            val userProject = user?.projects?.find { it.id_project == project.id_project }
            val task = userProject?.tasks?.find { it.id_tarea == updatedTask.id_tarea }
            task?.estado = updatedTask.estado

            // Guardar los cambios en el JSON
            val updatedJsonString = Gson().toJson(users)

            // Usar filesDir para obtener la ruta dinámica
            val filePath = File(filesDir, "json/data.json").absolutePath
            encriptarJSON(updatedJsonString, filePath)

            val resultIntent = Intent().apply {
                putExtra("taskChanged", true)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "Estado de la tarea actualizado", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al actualizar la tarea: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteProject() {
        try {
            // Cargar los usuarios desde el archivo
            val users = DataManager.loadUsers(this)

            val project = intent.extras?.getSerializable("project") as Project
            val userId = intent.extras?.getInt("userId") ?: return
            val user = users.find { it.id == userId }

            user!!.projects = user.projects?.filter { it.id_project != project.id_project } ?: emptyList()

            // Guardar los cambios en el JSON
            val updatedJsonString = Gson().toJson(users)

            // Usar filesDir para obtener la ruta dinámica
            val filePath = File(filesDir, "json/data.json").absolutePath
            encriptarJSON(updatedJsonString, filePath)

            setResult(Activity.RESULT_OK)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al eliminar el proyecto: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

