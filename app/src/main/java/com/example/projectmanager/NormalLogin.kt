package com.example.projectmanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class NormalLogin : AppCompatActivity() {

    private var clickCount = 0
    private val clickMax = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_normal)

        val loginButton = findViewById<Button>(R.id.ButtonLogin)
        val imageView = findViewById<ImageView>(R.id.imageView)

        loginButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))

        imageView.setOnClickListener {
            clickCount++

            if (clickCount == clickMax){
                val intent = Intent(this, AdministratorLogin::class.java)
                startActivity(intent)

                clickCount = 0
            }

            Handler().postDelayed({
                clickCount = 0
            }, 2000)

        }
    }
}