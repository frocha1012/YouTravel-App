package com.example.youtravel.model

 data class TravelRequest(
     private val user_id_admin: String? = "",
     private val category_id: Long = 0,
     private val title: String? = null,
     private val description: String? = null,
     private val date: String? = null,
     private val rating: String? = null,
     private val photo: String? = null
 )