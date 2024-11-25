package com.example.projectmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Serializable



class NormalLogin : AppCompatActivity() {

    private var clickCount = 0
    private val clickMax = 5
    private val gson = Gson()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_normal)

        val editTextUsername = findViewById<EditText>(R.id.EditTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.EditTextPassword)
        val loginButton = findViewById<Button>(R.id.ButtonLogin)
        val imageView = findViewById<ImageView>(R.id.imageViewLogo)
        val errorText = findViewById<TextView>(R.id.TextViewError)

        loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))

        //crearArchivo()
        val users = loadUsers()

        loginButton.setOnClickListener{
            val enteredUsername = editTextUsername.text.toString().trim()
            val enteredPassword = editTextPassword.text.toString().trim()

            val validUsers = users.find { it.usuario == enteredUsername &&
                    it.password == enteredPassword &&
                    it.rol == "usuario"}

            if (validUsers != null){
                val intent = Intent(this, AdministratorLogin::class.java)
                startActivity(intent)
            }else{
                errorText.visibility = View.VISIBLE
                editTextUsername.text = null
                editTextPassword.text = null
            }
        }

        imageView.setOnClickListener {
            clickCount++

            if (clickCount == clickMax){
                val intent = Intent(this, AdministratorLogin::class.java)
                intent.putExtra("users", users as Serializable)
                startActivity(intent)

                clickCount = 0
            }
            Handler().postDelayed({
                clickCount = 0
            }, 2000)
        }


    }

    private fun crearArchivo() {
        val gson = Gson()
        val users = listOf(
            User(1, "Juan Brito", "juanb", "juan1234", "desarrollador"),
            User(2, "Enric Antunez", "enrica", "enric1234", "usuario", listOf(
                Tarea(1, "prueba", 30, 30)
            ))
        )

        val jsonString = gson.toJson(users)

        val filepath = "/data/data/com.example.projectmanager/files/json/data.json"
        val file = File(filepath)
        FileWriter(file).use { writer ->
            writer.write(jsonString)
        }
    }

    private fun loadUsers(): List<User> {

        val file = File("/data/data/com.example.projectmanager/files/json/data.json")

        return try {
            val reader = FileReader(file)
            val content = reader.readText()
            reader.close()

            gson.fromJson(content, Array<User>::class.java).toList()
        } catch (e: Exception) {
            // Manejar la excepción, por ejemplo, mostrar un mensaje de error al usuario
            Log.e("Error", "Error al leer el archivo JSON: ${e.message}")
            emptyList()
        }
    }
}