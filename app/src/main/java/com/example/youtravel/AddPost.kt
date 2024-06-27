package com.example.youtravel

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AddPost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val imageView: ImageView = findViewById(R.id.image_thumbnail)
        val imageUriString: String? = intent.getStringExtra("imageUri")

        if (imageUriString != null) {
            val imageUri: Uri = Uri.parse(imageUriString)
            imageView.setImageURI(imageUri) // Display the image directly, assuming it's already sized appropriately
        } else {
            Log.e("AddPost", "Received null imageUri")
        }
    }
}
