package com.example.youtravel.api

import com.example.youtravel.model.UserDetails
import com.example.youtravel.model.UserDetailsInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @PUT("user/{userId}")
    fun updateUser(@Path("userId") userId: String, @Body userInfo: UserDetailsInfo): Call<UserDetailsInfo>

    @GET("user/{userId}")
    fun getUserDetails(@Path("userId") userId: Int): Call<UserDetails>

    @Multipart
    @POST("/userUpdateImage/{userId}")
    fun updateUserImage(@Path("userId") userId: Int): Call<UserDetails>

}