package com.example.youtravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Frontpage : AppCompatActivity() {

    private lateinit var getStarted: Button
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_frontpage)

        getStarted = findViewById(R.id.getStartedButton)
        login = findViewById(R.id.loginButton)

        if (isTokenPresent()){
            navigateToMainActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getStarted.setOnClickListener {
            navigateToNext()
        }
        login.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToNext(){
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("IntroViewed", false)) {
            navigateToRegister()
        } else {
            val intent = Intent(this, IntroMain::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToRegister(){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin(){
        val intent = Intent(this, Login::class.java)
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

    fun setIntroViewed() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putBoolean("IntroViewed", true)
            apply()
        }
    }
}