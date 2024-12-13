package com.example.projectmanager

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProjectInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        val textViewProjectName = findViewById<TextView>(R.id.ProjectName)
        val textViewDescription = findViewById<TextView>(R.id.ProjectDescription)
        val imageViewBack = findViewById<ImageView>(R.id.backButton)

        imageViewBack.setOnClickListener{
            finish()
        }

        val bundle = intent.extras

        val project = bundle!!.getSerializable("project") as Project

        textViewProjectName.text = project.name_project

    }
}