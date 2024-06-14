package com.example.youtravel

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Preview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val imageView: ImageView = findViewById(R.id.image_preview)
        val imageUri = intent.getParcelableExtra<Uri>("imageUri")
        imageView.setImageURI(imageUri)
    }
}
