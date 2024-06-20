package com.example.youtravel

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.youtravel.model.RegisterRequest
import com.example.youtravel.model.RegisterResponse
import com.example.youtravel.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Register : AppCompatActivity() {
    private lateinit var ageEditText: EditText
    private lateinit var nationalityEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.emailEditText)
        nameEditText = findViewById(R.id.nameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signUpButton = findViewById(R.id.signUpButton)
        ageEditText = findViewById(R.id.ageEditText)
        nationalityEditText = findViewById(R.id.nationalityEditText)
        loginButton = findViewById(R.id.haveAccountTextView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { navigateToFrontPage() }

        ageEditText.setOnClickListener { showDatePickerDialog(it) }
        nationalityEditText.setOnClickListener { showNationalityDialog(it) }

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val age = ageEditText.text.toString().toInt()
            val nationality = nationalityEditText.text.toString()

            val registerRequest = RegisterRequest(name, username, email, password, age, nationality)
            registerUser(registerRequest)
        }

        loginButton.setOnClickListener {
           navigateToLogin()
        }
    }

    private fun registerUser(registerRequest: RegisterRequest) {
        RetrofitClient.instance.signupUser(registerRequest).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Register, response.body()?.message, Toast.LENGTH_LONG).show()
                    navigateToLogin()
                } else {
                    Toast.makeText(this@Register, "Registration failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@Register, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToFrontPage() {
        val intent = Intent(this, Frontpage::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun showDatePickerDialog(view: View) {
        val currentCalendar = Calendar.getInstance()
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val age = currentYear - year
            ageEditText.setText(age.toString())
        }, currentYear, currentMonth, currentDay)

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
