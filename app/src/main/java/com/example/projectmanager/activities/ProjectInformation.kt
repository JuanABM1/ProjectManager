package com.example.projectmanager.activities

import com.example.projectmanager.adapters.FullTaskAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.dataModels.Project
import com.example.projectmanager.R
import com.example.projectmanager.dataModels.Task
import com.example.projectmanager.dataModels.User
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

    private fun updateTaskInJson(updatedTask: Task) {
        try {
            val file = File("/data/data/com.example.projectmanager/files/json/data.json")
            if (!file.exists()) {
                Toast.makeText(this, "El archivo de datos no existe", Toast.LENGTH_SHORT).show()
                return
            }

            val jsonString = file.readText()
            val users: MutableList<User> = Gson().fromJson(jsonString, object : TypeToken<MutableList<User>>() {}.type)

            val userId = intent.extras?.getInt("userId") ?: return
            val project = intent.extras?.getSerializable("project") as Project
            val user = users.find { it.id == userId }

            val userProject = user?.projects?.find { it.id_project == project.id_project }

            val task = userProject?.tasks?.find { it.id_tarea == updatedTask.id_tarea }
            task?.estado = updatedTask.estado

            val updatedJsonString = Gson().toJson(users)
            file.writeText(updatedJsonString)

            val resultIntent = Intent().apply {
                putExtra("taskChanged", true) // Información opcional para identificar qué cambió
            }
            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "Estado de la tarea actualizado", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al actualizar la tarea: ${e.message}", Toast.LENGTH_LONG).show()
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


            user!!.projects = user.projects?.filter { it.id_project != project.id_project } ?: emptyList()

            val updatedJsonString = Gson().toJson(users)
            file.writeText(updatedJsonString)


            setResult(Activity.RESULT_OK)
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al eliminar el proyecto: ${e.message}", Toast.LENGTH_LONG)
                .show()

        }
    }
}
