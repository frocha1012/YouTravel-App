package com.example.youtravel.model

 data class TravelRequest(
     private val user_id_admin: Int = 0,
     private val category_id: Int = 0,
     private val title: String? = null,
     private val description: String? = null,
     private val date: String? = null,
     private val rating: String? = null,
     private val photo: String? = null
 )