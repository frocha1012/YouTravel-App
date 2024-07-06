package com.example.youtravel

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yalantis.ucrop.UCrop
import java.io.File

class AddStop : AppCompatActivity() {
    private lateinit var map: GoogleMap
    private var selectedLocation: LatLng? = null
    private lateinit var imageViewStopPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stop)

        val imageViewStopPhoto: ImageView = findViewById(R.id.imageViewStopPhoto)
        val editTextStopTitle: EditText = findViewById(R.id.editTextStopTitle)
        val buttonSaveStop: Button = findViewById(R.id.buttonSaveStop)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            map.uiSettings.isZoomControlsEnabled = true
            map.setOnMapClickListener { latLng ->
                // Save the clicked location and update the marker
                selectedLocation = latLng
                map.clear() // Clear previous markers
                map.addMarker(MarkerOptions().position(latLng).title("Selected Stop Location"))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }

        imageViewStopPhoto.setOnClickListener {
            selectImage()
        }

        buttonSaveStop.setOnClickListener {
            val stopTitle = editTextStopTitle.text.toString()
            val stopPhotoUri = selectedLocation // Assuming you're storing the URI from UCrop

            if (stopTitle.isNotEmpty() && stopPhotoUri != null && selectedLocation != null) {
                // Implement your save logic here, for example:
                // saveStopToDatabase(stopTitle, stopPhotoUri, selectedLocation)
                Toast.makeText(this, "Stop saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                UCrop.of(uri, Uri.fromFile(File(cacheDir, "tempCropped.jpg")))
                    .withAspectRatio(16F, 16F)
                    .start(this)
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                imageViewStopPhoto.setImageBitmap(bitmap)
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}
