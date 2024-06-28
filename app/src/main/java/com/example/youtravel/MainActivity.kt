package com.example.youtravel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_new_post -> {
                    startActivity(Intent(this, Camera::class.java))
                    true
                }
                R.id.navigation_feed -> {
                    // Prevent restarting the same activity
                    if (this !is MainActivity) {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    true
                }
                R.id.navigation_user_edit -> {
                    startActivity(Intent(this, UserEdit::class.java))
                    true
                }
                R.id.navigation_logout -> {
                    clearToken()
                    navigateToLogin()
                    true
                }
                else -> false
            }
        }

        // Ensure correct menu item is highlighted
        bottomNavigationView.selectedItemId = R.id.navigation_feed

        // Load the Travels fragment by default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Travels())
                .commit()
        }


    }
    private fun clearToken() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().remove("token").apply()
    }
    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}
