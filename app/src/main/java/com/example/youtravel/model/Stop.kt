package com.example.youtravel.model

import android.net.Uri

data class Stop(
    val title: String,
    val imageUri: Uri  // Use String if you're using image URLs instead
)
