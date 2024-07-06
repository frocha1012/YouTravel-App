package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TravelDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_travel_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve the passed travel title from the intent
        val travelTitle = intent.getStringExtra("travelTitle")

        // Set the travel title
        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        textViewTitle.text = travelTitle ?: "Default Title"

        // Setup the Add Stop button
        val btnAddStop: Button = findViewById(R.id.btnAddStop)
        btnAddStop.setOnClickListener {
            val intent = Intent(this, AddStop::class.java)
            intent.putExtra("travelTitle", travelTitle)  // Pass the travel title to the AddStopActivity
            startActivity(intent)
        }
    }
}
