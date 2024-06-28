package com.example.youtravel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Camera : AppCompatActivity() {

    private lateinit var imageUri: Uri

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            startPreviewActivity(imageUri)
        }
    }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { startPreviewActivity(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        findViewById<ImageView>(R.id.buttonBack).setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_new_post -> {
                    //current
                    true
                }
                R.id.navigation_feed -> {
                    startActivity(Intent(this, MainActivity::class.java))
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
        // Set the current item without animation to avoid reload
        bottomNavigationView.selectedItemId = R.id.navigation_new_post
    }

    fun takePicture(view: View) {
        imageUri = createImageFileUri()
        takePictureLauncher.launch(imageUri)
    }

    fun chooseFromGallery(view: View) {
        pickImageLauncher.launch("image/*")
    }

    private fun startPreviewActivity(imageUri: Uri) {
        Intent(this, Preview::class.java).also { intent ->
            intent.putExtra("imageUri", imageUri.toString()) // Pass URI as string
            startActivity(intent)
        }
    }

    private fun createImageFileUri(): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val imageFile = File(storageDir, "JPEG_${timeStamp}.jpg")
        return FileProvider.getUriForFile(this, "com.example.youtravel.fileprovider", imageFile)
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
