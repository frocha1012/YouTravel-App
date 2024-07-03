package com.example.youtravel.api

import com.example.youtravel.model.LoginRequest
import com.example.youtravel.model.LoginResponse
import com.example.youtravel.model.RegisterRequest
import com.example.youtravel.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signupUser(@Body signupRequest: RegisterRequest): Call<RegisterResponse>
}