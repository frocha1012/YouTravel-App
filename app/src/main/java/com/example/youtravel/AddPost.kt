package com.example.youtravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.textfield.TextInputEditText
import com.google.android.libraries.places.api.net.PlacesClient


class AddPost : AppCompatActivity() {

    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        val imageView: ImageView = findViewById(R.id.image_thumbnail)
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
}
