package com.example.projectmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class AdministratorLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator_login)

        val backButton = findViewById<ImageView>(R.id.imageViewBack)
        val loginButton = findViewById<Button>(R.id.ButtonLogin)
        val usernameEditText = findViewById<EditText>(R.id.EditTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.EditTextPassword)

        loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))

        val users: ArrayList<User> = intent.getSerializableExtra("users") as ArrayList<User>


        loginButton.setOnClickListener{
            val enteredUsername = usernameEditText.text.toString().trim()
            val enteredPassword = passwordEditText.text.toString().trim()

            val validUsers = users.find { it.usuario == enteredUsername &&
                    it.password == enteredPassword &&
                    it.rol == "desarrollador"}

            if (validUsers != null){
                backToLogin()
            }else{

            }

        }
        backButton.setOnClickListener{
            backToLogin()
        }
    }

    private fun backToLogin() {
        val intent = Intent(this, NormalLogin::class.java)

        startActivity(intent)
    }


}