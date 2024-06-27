package com.example.youtravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.yalantis.ucrop.UCrop
import java.io.File

class Preview : AppCompatActivity() {

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString)
            startCrop(imageUri)
        } else {
            Log.e("Preview", "Received null imageUri")
        }

        // Set up the Back button to navigate back to the Camera activity
        findViewById<Button>(R.id.button_back).setOnClickListener {
            // Option 1: Explicitly start the Camera activity (if it should restart)
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)

            // Option 2: Simply finish the current activity to go back to the previous one
            // finish()
        }
    }

    private fun startCrop(imageUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))
        UCrop.of(imageUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            val resultUri = UCrop.getOutput(data)
            findViewById<ImageView>(R.id.image_preview).setImageURI(resultUri)

            findViewById<Button>(R.id.button_next).setOnClickListener {
                val intent = Intent(this, AddPost::class.java).apply {
                    // Ensure you're passing the URI as a string correctly
                    putExtra("imageUri", resultUri.toString())
                }
                startActivity(intent)
            }
        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            val cropError = UCrop.getError(data)
            Log.e("Preview", "Crop error: $cropError")
        }
    }
}
