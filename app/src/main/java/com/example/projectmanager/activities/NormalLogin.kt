package com.example.projectmanager.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.projectmanager.dataModels.Project
import com.example.projectmanager.R
import com.example.projectmanager.dataModels.Task
import com.example.projectmanager.dataModels.User
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Serializable
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class NormalLogin : AppCompatActivity() {

    private val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_normal)

        val editTextUsername = findViewById<EditText>(R.id.EditTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.EditTextPassword)
        val loginButton = findViewById<Button>(R.id.ButtonLogin)
        val errorText = findViewById<TextView>(R.id.TextViewError)

        loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))

        crearArchivo()
        val users = loadUsers()

        loginButton.setOnClickListener{
            val enteredUsername = editTextUsername.text.toString().trim()
            val enteredPassword = editTextPassword.text.toString().trim()

            val validUser = users.find { it.usuario == enteredUsername &&
                    it.password == enteredPassword}

            if (validUser != null){
                val intent = Intent(this, MainPageUsers::class.java)
                intent.putExtra("users", users as Serializable)
                intent.putExtra("user", validUser)
                startActivity(intent)
            }else{
                errorText.visibility = View.VISIBLE
                editTextUsername.text = null
                editTextUsername.setHintTextColor(Color.RED)
                editTextPassword.text = null
                editTextPassword.setHintTextColor(Color.RED)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearArchivo() {

        val users = listOf(
            User(
                id = 1,
                nombre = "Juan Brito",
                usuario = "admin",
                password = "admin",
                projects = emptyList()
            ),
            User(
                id = 2,
                nombre = "CEP",
                usuario = "cep",
                password = "informatica",
                projects = listOf(
                    Project(
                        id_project = 1,
                        name_project = "Proyecto A",
                        tasks = listOf(
                            Task(
                                id_tarea = 1,
                                nombre_tarea = "Tarea de prueba",
                                fecha_final = "2024-11-25",
                                descripcion = "descripcion primera tarea",
                                estado = "en progreso"
                            ),
                            Task(
                                id_tarea = 2,
                                nombre_tarea = "tarea de prueba 2",
                                fecha_final = "2024-12-10",
                                descripcion = "descripción segunda tarea",
                                estado = "pendiente"
                            )
                        )
                    ),
                    Project(
                        id_project = 2,
                        name_project = "Proyecto B",
                        tasks = listOf(
                            Task(
                                id_tarea = 3,
                                nombre_tarea = "Tarea de prueba 3",
                                fecha_final = "2024-11-30",
                                descripcion = "descripción tarea 3",
                                estado = "en progreso"
                            ),
                            Task(
                                id_tarea = 4,
                                nombre_tarea = "tarea de prueba 4",
                                fecha_final = "2024-12-01",
                                descripcion = "descripcion tarea 4",
                                estado = "pendiente"
                            )
                        )
                    )
                )
            )
        )

        val processedUsers = users.map { user ->
            User(
                id = user.id,
                nombre = user.nombre,
                usuario = user.usuario,
                password = user.password,
                projects = user.projects?.map { project ->
                    Project(
                        id_project = project.id_project,
                        name_project = project.name_project,
                        tasks = project.tasks.map { task ->
                            Task(
                                id_tarea = task.id_tarea,
                                nombre_tarea = task.nombre_tarea,
                                fecha_final = task.fecha_final,
                                descripcion = task.descripcion,
                                estado = task.estado.ifEmpty { "pendiente" }
                            )
                        }
                    )
                }
            )
        }

        // Convierte a JSON
        val jsonString = gson.toJson(processedUsers)

        val dir = filesDir
        val filepath = "$dir/json/data.json"

        val jsonDir = File(dir, "json")
        if (!jsonDir.exists()) {
            jsonDir.mkdirs() // Crea el directorio si no existe
        }


        encriptarJSON(jsonString, filepath)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encriptarJSON(jsonString: String?, filepath: String) {
        val fixedKey = "1234567890123456".toByteArray(Charsets.UTF_8)
        val secretKey = SecretKeySpec(fixedKey, "AES")


        val iv = ByteArray(16)
        java.security.SecureRandom().nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

        val encryptedBytes = cipher.doFinal(jsonString!!.toByteArray(Charsets.UTF_8))

        val encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes)
        val ivBase64 = Base64.getEncoder().encodeToString(iv)

        val file = File(filepath)
        FileWriter(file).use { writer ->
            writer.write("EncryptedData:$encryptedBase64\n")
            writer.write("IV:$ivBase64\n")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadUsers(): List<User> {
        val dir = filesDir
        val file = File(dir, "/json/data.json")

        return try {
            val content = file.readText()

            val encryptedData = content.substringAfter("EncryptedData:").substringBefore("\n").trim()
            val iv = content.substringAfter("IV:").trim()

            val encryptedBytes = Base64.getDecoder().decode(encryptedData)
            val ivBytes = Base64.getDecoder().decode(iv)

            val fixedKey = "1234567890123456".toByteArray(Charsets.UTF_8)
            val secretKey = SecretKeySpec(fixedKey, "AES")
            val ivSpec = IvParameterSpec(ivBytes)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

            val decryptedBytes = cipher.doFinal(encryptedBytes)
            val decryptedJson = String(decryptedBytes, Charsets.UTF_8)

            gson.fromJson(decryptedJson, Array<User>::class.java).toList()
        } catch (e: Exception) {
            Log.e("Error", "Error al leer o desencriptar el archivo JSON", e)
            emptyList()
        }
    }

}