package com.example.youtravel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.youtravel.config.Jwt
import com.example.youtravel.model.TravelRequest
import com.example.youtravel.model.TravelResponse
import com.example.youtravel.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddPost : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var publishButton: Button
    private lateinit var ratingBar: RatingBar
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        titleEditText = findViewById(R.id.title_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        publishButton = findViewById(R.id.publish_button)
        ratingBar = findViewById(R.id.rating_bar)
        imageView = findViewById(R.id.image_thumbnail)

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri: Uri = Uri.parse(imageUriString)
            imageView.setImageURI(imageUri)

            publishButton.setOnClickListener {
                uploadImage(imageUri) { imageUrl ->
                    if (imageUrl != null) {
                        postTravel(imageUrl)
                        Toast.makeText(this, "Transaction made with success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Error: No image URI passed to AddPost", Toast.LENGTH_LONG).show()
            Log.e("AddPost", "No image URI passed to activity")
        }
    }


    private fun uploadImage(imageUri: Uri, callback: (String?) -> Unit) {
        val filePath = getRealPathFromURI(imageUri)
        if (filePath == null) {
            Log.e("Upload Image", "Real path is null")
            callback(null)
            return
        }

        // Load the image from the file path
        val bitmap = BitmapFactory.decodeFile(filePath)

// Convert bitmap to PNG and save it to a temporary file with a unique timestamp in the filename
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val pngFile = File(cacheDir, "upload_image_$timestamp.png")
        FileOutputStream(pngFile).use { outStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
        }

        val requestBody = pngFile.asRequestBody("image/png".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("fileUpload", pngFile.name, requestBody)

        RetrofitClient.instance.uploadImage(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.string()
                    Log.d("Upload Image", "Image URL: $imageUrl")
                    callback(imageUrl)
                } else {
                    Log.e("Upload Image", "Upload failed: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Upload Image", "Error: ${t.message}")
                callback(null)
            }
        })
    }


    private fun getRealPathFromURI(contentUri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(contentUri)
            val file = File(cacheDir, "temp_image")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            file.absolutePath
        } catch (e: Exception) {
            Log.e("GetRealPath", "Error getting real path", e)
            null
        }
    }


    private fun postTravel(imageUrl: String) {
        val travelRequest = TravelRequest(
            user_id_admin = "known-good-user-id",
            title = "Test Title",
            description = "Test Description",
            rating = "5",
            photo = imageUrl
        )

        RetrofitClient.instance.createTravel(travelRequest).enqueue(object : Callback<TravelResponse> {
            override fun onResponse(call: Call<TravelResponse>, response: Response<TravelResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddPost, "Travel created successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AddPost, "Failed to create travel: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TravelResponse>, t: Throwable) {
                Toast.makeText(this@AddPost, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
