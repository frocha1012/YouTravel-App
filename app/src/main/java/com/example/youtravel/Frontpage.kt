package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Frontpage : AppCompatActivity() {

    private lateinit var getStarted: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_frontpage)


        getStarted = findViewById(R.id.getStartedButton)

        if (isTokenPresent()){
            navigateToMainActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getStarted.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToRegister(){
        //val intent = Intent(this, Intro2::class.java)
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }


    private fun navigateToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isTokenPresent(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        return token != null
    }

}