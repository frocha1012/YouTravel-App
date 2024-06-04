package com.example.youtravel

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class Register : AppCompatActivity() {
    private lateinit var ageEditText: EditText
    private lateinit var nationalityEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Setup edge-to-edge UI handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup views
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { navigateToFrontPage() }

        ageEditText = findViewById(R.id.ageEditText)
        nationalityEditText = findViewById(R.id.nationalityEditText)

        // Setup handlers for age and nationality fields
        ageEditText.setOnClickListener { showDatePickerDialog(it) }
        nationalityEditText.setOnClickListener { showNationalityDialog(it) }
    }

    private fun navigateToFrontPage() {
        val intent = Intent(this, Frontpage::class.java)
        startActivity(intent)
        finish()
    }

    private fun showDatePickerDialog(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            ageEditText.setText(String.format(Locale.US, "%d-%d-%d", dayOfMonth, monthOfYear + 1, year))
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showNationalityDialog(view: View) {
        val items = arrayOf(
            "Albania", "Andorra", "Armenia", "Austria", "Azerbaijan",
            "Belarus", "Belgium", "Bosnia and Herzegovina", "Bulgaria",
            "Croatia", "Cyprus", "Czech Republic", "Denmark",
            "Estonia", "Finland", "France", "Georgia", "Germany", "Greece",
            "Hungary", "Iceland", "Ireland", "Italy", "Kazakhstan",
            "Kosovo", "Latvia", "Liechtenstein", "Lithuania", "Luxembourg",
            "Malta", "Moldova", "Monaco", "Montenegro", "Netherlands",
            "North Macedonia", "Norway", "Poland", "Portugal", "Romania",
            "Russia", "San Marino", "Serbia", "Slovakia", "Slovenia",
            "Spain", "Sweden", "Switzerland", "Turkey", "Ukraine",
            "United Kingdom", "Vatican City"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Nationality")
        builder.setItems(items) { dialog, which ->
            nationalityEditText.setText(items[which])
        }
        builder.show()
    }
}
