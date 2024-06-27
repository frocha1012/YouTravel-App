package com.example.youtravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
}
