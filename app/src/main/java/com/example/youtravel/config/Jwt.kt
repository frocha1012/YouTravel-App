package com.example.youtravel.config

import android.content.Context
import android.util.Base64
import org.json.JSONException
import org.json.JSONObject

class Jwt {

    fun getUserID(context: Context): Int {
        val token = getToken(context)
        val userIdString = token?.let { getUserIdFromToken(it) }
        // Convert the string to an integer, returning 0 or a default value in case of failure
        return userIdString?.toIntOrNull() ?: throw IllegalArgumentException("User ID is invalid or not found.")
    }

    private fun decodeJWT(token: String): String? {
        return try {
            val parts = token.split(".")
            val base64EncodedBody = parts[1]
            val base64DecodedBytes = Base64.decode(base64EncodedBody, Base64.URL_SAFE)
            String(base64DecodedBytes, charset("UTF-8"))
        } catch (e: Exception) {
            null
        }
    }

    private fun getUserIdFromToken(token: String): String? {
        val jsonPayload = decodeJWT(token)
        jsonPayload?.let {
            try {
                val jsonObject = JSONObject(it)
                return jsonObject.getString("sub")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}