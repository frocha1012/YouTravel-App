package com.example.youtravel.api

import com.example.youtravel.model.Category
import com.example.youtravel.model.LoginRequest
import com.example.youtravel.model.LoginResponse
import com.example.youtravel.model.RegisterRequest
import com.example.youtravel.model.RegisterResponse
import com.example.youtravel.model.Travel
import com.example.youtravel.model.TravelRequest
import com.example.youtravel.model.TravelResponse
import com.example.youtravel.model.User
import com.example.youtravel.model.UserDetails
import com.example.youtravel.model.UserDetailsInfo
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ApiService {

    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signupUser(@Body signupRequest: RegisterRequest): Call<RegisterResponse>

    @POST("travel")
    fun createTravel(@Body travelRequest: TravelRequest): Call<TravelResponse>

    @Multipart
    @POST("upload")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ResponseBody>

    @GET("categories")
    fun getCategories(): Call<List<Category>>

    @GET("userTravel/{userId}")
    fun getTravelsByUserId(@Path("userId") userId: Int): Call<List<Travel>>

    @Streaming
    @GET("travelImage/{travelId}")
    fun getTravelImage(@Path("travelId") travelId: String): Call<ResponseBody>

    @PUT("user/{userId}")
    fun updateUser(@Path("userId") userId: String, @Body userInfo: UserDetailsInfo): Call<UserDetailsInfo>

    @GET("user/{userId}")
    fun getUserDetails(@Path("userId") userId: Int): Call<UserDetails>
}