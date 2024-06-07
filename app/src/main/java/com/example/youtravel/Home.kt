package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val clearTokenButton = findViewById<Button>(R.id.clearTokenButton)
        val addTravelButton = findViewById<Button>(R.id.addTravelButton)
        clearTokenButton.setOnClickListener {
            clearToken()
        }

        clearTokenButton.setOnClickListener {
            clearToken()
        }

        addTravelButton.setOnClickListener {
            navigateToAddTravel()
        }
    }


    private fun clearToken() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("token")
            apply()
        }
        Toast.makeText(this, "Token cleared", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAddTravel() {
        val intent = Intent(this, AddTravel::class.java)
        startActivity(intent)
        finish()
    }

}