package com.example.youtravel.model

data class Travel(
    val ID: Long,
    val UserIDAdmin: Long,
    val CategoryID: Long,
    val Description: String,
    val Date: String,
    val Rating: String,
    val User: User,
    val Category: Category
)
