package com.example.youtravel
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.youtravel.config.Jwt
import com.example.youtravel.model.Category
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
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.textfield.TextInputEditText
import com.google.android.libraries.places.api.net.PlacesClient

class AddPost : AppCompatActivity() {

    private lateinit var placesClient: PlacesClient
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var publishButton: Button
    private lateinit var ratingBar: RatingBar
    private lateinit var imageView: ImageView
    private lateinit var categorySpinner: Spinner

    private var categoryList: List<Category> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        findViewById<ImageView>(R.id.buttonBack).setOnClickListener{
            startActivity(Intent(this, Camera::class.java))
            finish()
        }

        categorySpinner = findViewById(R.id.category_spinner)
        loadCategories()

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)
        val inputEditText: TextInputEditText = findViewById(R.id.places_autocomplete_edittext)
        inputEditText.setOnClickListener {
            startAutocompleteActivity()
        }
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
    private fun startAutocompleteActivity() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val place = Autocomplete.getPlaceFromIntent(data)
            val editText: TextInputEditText = findViewById(R.id.places_autocomplete_edittext)
            editText.setText(place.address)
        } else if (resultCode == AutocompleteActivityMode.PARCELABLE_WRITE_RETURN_VALUE) {
            val status: Status = Autocomplete.getStatusFromIntent(data!!)
            Log.e("AddPost", "Error: ${status.statusMessage}")
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
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
                    callback(pngFile.name)
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


    private fun postTravel(fileName: String) {
        val selectedCategoryId = categoryList[categorySpinner.selectedItemPosition].category_id
        val userId = Jwt().getUserID(this)

        Log.d("AddPost", "UserID: $userId")


        val travelRequest = TravelRequest(
            user_id_admin = userId,
            category_id = selectedCategoryId,
            title = titleEditText.text.toString(),
            description = descriptionEditText.text.toString(),
            date = "2024-06-01",
            rating = ratingBar.rating.toString(),
            photo = fileName
        )

        RetrofitClient.instance.createTravel(travelRequest).enqueue(object : Callback<TravelResponse> {
            override fun onResponse(call: Call<TravelResponse>, response: Response<TravelResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddPost, "Travel created successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Post Travel", "Failed to create travel: $errorBody")
                    Toast.makeText(this@AddPost, "Failed to create travel: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<TravelResponse>, t: Throwable) {
                Log.e("Post Travel", "Error: ${t.message}")
                Toast.makeText(this@AddPost, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Initialize Places Client if not already done
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        val imageUriString: String? = intent.getStringExtra("imageUri")
        imageUriString?.let {
            val imageUri: Uri = Uri.parse(it)
            imageView.setImageURI(imageUri)
        } ?: Log.e("AddPost", "Received null imageUri")

        val inputEditText: TextInputEditText = findViewById(R.id.places_autocomplete_edittext)
        inputEditText.setOnClickListener {
            startAutocompleteActivity()
        }
    }

    private fun loadCategories() {
        RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        categoryList = categories
                        val adapter = ArrayAdapter(
                            this@AddPost,
                            android.R.layout.simple_spinner_item,
                            categoryList.map { it.description }
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        categorySpinner.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@AddPost, "Failed to fetch categories", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(this@AddPost, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}