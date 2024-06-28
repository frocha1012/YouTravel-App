package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_new_post -> {
                    startActivity(Intent(this, Camera::class.java))
                    true
                }
                R.id.navigation_feed -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_user_edit -> {
                    // This is the current activity, no need to do anything
                    true
                }
                else -> false
            }
        }
        // Set the current item without animation to avoid reload
        bottomNavigationView.selectedItemId = R.id.navigation_user_edit
    }
}
