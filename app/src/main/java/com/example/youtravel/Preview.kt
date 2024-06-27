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

        val imageUriString = intent.getStringExtra("imageUri") // Get the URI as a String
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString) // Convert the String back to URI
            startCrop(imageUri)
        } else {
            // Handle error if imageUri is null
            Log.e("Preview", "Received null imageUri")
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

        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK && data != null) {
                val resultUri = UCrop.getOutput(data)
                if (resultUri != null) {
                    findViewById<ImageView>(R.id.image_preview).setImageURI(resultUri)

                    findViewById<Button>(R.id.button_next).setOnClickListener {
                        Log.d("Preview", "Next button clicked")
                        val intent = Intent(this, AddPost::class.java).apply {
                            putExtra("imageUri", resultUri.toString()) // Pass the URI as a String
                        }
                        startActivity(intent)
                    }
                } else {
                    Log.e("Preview", "Cropped image URI is null")
                }
            } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
                val cropError = UCrop.getError(data)
                Log.e("Preview", "Crop error: $cropError")
                // Handle the cropping error
            }
        }
    }
}
