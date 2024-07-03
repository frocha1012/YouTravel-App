package com.example.youtravel.api

import com.example.youtravel.model.Category
import com.example.youtravel.model.Travel
import com.example.youtravel.model.TravelRequest
import com.example.youtravel.model.TravelResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

interface TravelService {

    @GET("categories")
    fun getCategories(): Call<List<Category>>

    @GET("userTravel/{userId}")
    fun getTravelsByUserId(@Path("userId") userId: Int): Call<List<Travel>>

    @Streaming
    @GET("travelImage/{travelId}")
    fun getTravelImage(@Path("travelId") travelId: String): Call<ResponseBody>

    @POST("travel")
    fun createTravel(@Body travelRequest: TravelRequest): Call<TravelResponse>
}