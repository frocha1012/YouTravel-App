package com.example.youtravel.api

import com.example.youtravel.model.LoginRequest
import com.example.youtravel.model.LoginResponse
import com.example.youtravel.model.RegisterRequest
import com.example.youtravel.model.RegisterResponse
import com.example.youtravel.model.TravelRequest
import com.example.youtravel.model.TravelResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signupUser(@Body signupRequest: RegisterRequest): Call<RegisterResponse>

    @POST
    fun createTravel(@Body travelRequest: TravelRequest): Call<TravelResponse>

    @Multipart
    @POST("upload")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ResponseBody>
}