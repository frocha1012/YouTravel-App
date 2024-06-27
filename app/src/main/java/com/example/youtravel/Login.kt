package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.youtravel.model.LoginRequest
import com.example.youtravel.model.LoginResponse
import com.example.youtravel.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.inputEmail)
        val passwordEditText = findViewById<EditText>(R.id.inputPassword)
        val loginButton = findViewById<Button>(R.id.buttonCustom)

        val gotoRegisterButton = findViewById<TextView>(R.id.forgotPassword)
        val backButton = findViewById<ImageButton>(R.id.buttonChevronLeft)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_LONG).show()
            }
        }

        gotoRegisterButton.setOnClickListener {
            navigateToRegister()
        }

        backButton.setOnClickListener {
            navigateToFrontPage()
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        RetrofitClient.instance.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.message.isNotEmpty()) {
                        storeToken(loginResponse.message)
                        Toast.makeText(this@Login, "Login successful", Toast.LENGTH_LONG).show()
                        navigateToHome()
                    } else {
                        Toast.makeText(this@Login, "Login failed: empty response", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Login failed with error code: ${response.code()} and error message: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun navigateToRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, Intro2::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToFrontPage() {
        val intent = Intent(this, Frontpage::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun storeToken(token: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("token", null)  // Returns null if token does not exist
    }
}