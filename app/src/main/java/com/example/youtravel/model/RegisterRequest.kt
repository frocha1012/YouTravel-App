package com.example.youtravel.model

data class RegisterRequest(
    val nome: String,
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val age: Int,
    val nationality: String
)
