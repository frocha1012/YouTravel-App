package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Intro5 : AppCompatActivity() {
    private lateinit var next_page: Button
    private lateinit var previous_page: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro5)

        next_page = findViewById(R.id.buttonProximo)
        next_page.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        previous_page = findViewById(R.id.buttonAnterior)
        previous_page.setOnClickListener {
            val intent = Intent(this, Intro4::class.java)
            startActivity(intent)
            finish()
        }
    }
}