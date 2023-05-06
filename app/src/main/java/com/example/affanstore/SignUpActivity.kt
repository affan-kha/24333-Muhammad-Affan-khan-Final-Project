package com.example.affanstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    lateinit var firstName: TextInputLayout
    lateinit var lastName: TextInputLayout
    lateinit var userName: TextInputLayout
    lateinit var email: TextInputLayout
    lateinit var phone: TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var city: TextInputLayout
    lateinit var street: TextInputLayout
    lateinit var streetNumber: TextInputLayout
    lateinit var zipCode: TextInputLayout
    lateinit var signUpBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        userName = findViewById(R.id.user_name)
        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)
        password = findViewById(R.id.password)
        city = findViewById(R.id.city)
        street = findViewById(R.id.street)
        streetNumber = findViewById(R.id.street_number)
        zipCode = findViewById(R.id.zipcode)
        signUpBtn = findViewById(R.id.create_account_btn)

        signUpBtn.setOnClickListener {

            val firstNameValue = firstName.editText!!.text.toString()
            val lastNameValue = lastName.editText!!.text.toString()
            val userNameValue = userName.editText!!.text.toString()
            val emailValue = email.editText!!.text.toString()
            val phoneValue = phone.editText!!.text.toString()
            val passwordValue = password.editText!!.text.toString()
            val cityValue = city.editText!!.text.toString()
            val streetValue = street.editText!!.text.toString()
            val streetNumberValue = streetNumber.editText!!.text.toString()
            val zipcodeValue = zipCode.editText!!.text.toString()

            if (firstNameValue.isEmpty() || lastNameValue.isEmpty() ||
                userNameValue.isEmpty() ||
                emailValue.isEmpty() ||
                phoneValue.isEmpty() ||
                passwordValue.isEmpty() ||
                cityValue.isEmpty() ||
                streetValue.isEmpty() ||
                streetNumberValue.isEmpty() ||
                zipcodeValue.isEmpty()
            ) {
                Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show()
            } else {
                Utils.startLoading(this)
                val queue = Volley.newRequestQueue(this)
                val url = "https://fakestoreapi.com/users"

                val user = User(
                    Address(
                        cityValue,
                        Geolocation("0.00", "0.00"),
                        streetNumberValue.toInt(),
                        streetValue,
                        zipcodeValue
                    ), emailValue,
                    Name(firstNameValue, lastNameValue), passwordValue, phoneValue, userNameValue
                )
                val gson = Gson()
                val requestJson = JSONObject(gson.toJson(user))
                val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, requestJson,
                    { response ->
                        Utils.dismiss()
                        Log.d("TEST199", response.toString())
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    },
                    { error ->
                        Utils.dismiss()
                        try {
                            Log.d("TEST199", error!!.localizedMessage!!)
                        }
                        catch (e:java.lang.Exception){
                            e.printStackTrace()
                        }
                    }
                )

                queue.add(jsonObjectRequest)
            }
        }

    }
}