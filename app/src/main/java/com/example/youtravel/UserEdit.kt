package com.example.youtravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.youtravel.config.Jwt
import com.example.youtravel.model.UserDetails
import com.example.youtravel.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserEdit : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var avatarImageView: ImageView
    private lateinit var avatarButton: ImageView
    private lateinit var chooseFromGalleryLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        nomeEditText = findViewById(R.id.nameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        updateButton = findViewById(R.id.confirmEditButton)
        avatarImageView = findViewById(R.id.imageView3)
        avatarButton = findViewById(R.id.avatarButton)

        findViewById<ImageView>(R.id.buttonBack).setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setupBottomNavigationView()
        loadUserData()
        setupGalleryPicker()

        avatarButton.setOnClickListener {
            chooseFromGalleryLauncher.launch("image/*")
        }
    }

    private fun setupGalleryPicker() {
        chooseFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                startCrop(it)
            }
        }
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "JPEG_${System.currentTimeMillis()}.jpg"))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            val resultUri = UCrop.getOutput(data)
            avatarImageView.setImageURI(resultUri)
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
        RetrofitClient.userService.getUserDetails(userId).enqueue(object : Callback<UserDetails> {
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
