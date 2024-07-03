package com.example.youtravel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        // Check token and permissions at startup
        if (isTokenPresent()) {
            navigateToMainActivity()
        } else {
            checkPermissions()
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

    private fun checkPermissions() {
        val permissionsNeeded = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA
        )
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                Toast.makeText(this, "All permissions granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions are required for proper app functionality.", Toast.LENGTH_LONG).show()
            }
        }

        val shouldRequestPermissions = permissionsNeeded.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (shouldRequestPermissions) {
            requestPermissionLauncher.launch(permissionsNeeded)
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
}