package com.example.youtravel.model

data class TravelResponse (
    private val id: Long = 0,
    private val user_id_admin: Long = 0,
    private val category_id: Long = 0,
    private val title: String? = null,
    private val description: String? = null,
    private val date: String? = null,
    private val rating: String? = null
)