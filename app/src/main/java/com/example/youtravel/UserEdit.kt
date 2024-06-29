package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.youtravel.config.Jwt
import com.example.youtravel.model.UserDetails
import com.example.youtravel.model.UserDetailsInfo
import com.example.youtravel.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserEdit : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        nomeEditText = findViewById(R.id.nameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        updateButton = findViewById(R.id.confirmEditButton)

        setupBottomNavigationView()
        loadUserData()

        updateButton.setOnClickListener {
            updateUserDetails()
        }
    }

    private fun setupBottomNavigationView() {
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
                R.id.navigation_user_edit -> true
                R.id.navigation_user_edit -> {
                    // This is the current activity, no need to do anything
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
        bottomNavigationView.selectedItemId = R.id.navigation_user_edit
    }

    private fun loadUserData() {
        val userId = Jwt().getUserID(this)
        RetrofitClient.instance.getUserDetails(userId).enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.isSuccessful) {
                    response.body()?.let { userDetails ->
                        nomeEditText.setText(userDetails.nome)
                        usernameEditText.setText(userDetails.username)
                        Toast.makeText(this@UserEdit, "User details loaded successfully.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@UserEdit, "Failed to load user details: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                Toast.makeText(this@UserEdit, "Network error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateUserDetails() {
        val userId = Jwt().getUserID(this)
        val userInfo = UserDetailsInfo(
            nome = nomeEditText.text.toString(),
            username = usernameEditText.text.toString()
        )

        RetrofitClient.instance.updateUser(userId.toString(), userInfo)
            .enqueue(object : Callback<UserDetailsInfo> {
                override fun onResponse(
                    call: Call<UserDetailsInfo>,
                    response: Response<UserDetailsInfo>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UserEdit,
                            "Details updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@UserEdit,
                            "Failed to update details: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserDetailsInfo>, t: Throwable) {
                    Toast.makeText(
                        this@UserEdit,
                        "Network error: ${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
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

