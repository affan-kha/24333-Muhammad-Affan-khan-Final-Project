package com.example.affanstore

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var username: TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var loginBtn: MaterialButton
    lateinit var appSettings: AppSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
         appSettings = AppSettings(this)
        username = findViewById(R.id.login_username)
        password = findViewById(R.id.login_password)

        loginBtn = findViewById(R.id.login_login_btn)

        loginBtn.setOnClickListener {
            val usernameValue = username.editText!!.text.toString()
            val passwordValue = password.editText!!.text.toString()

            if (usernameValue.isEmpty() || passwordValue.isEmpty()){
                Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show()
            }
            else{
                Utils.startLoading(this)
                val queue = Volley.newRequestQueue(this)
                val url = "https://fakestoreapi.com/auth/login"

                val requestJson = JSONObject()
                requestJson.put("username",usernameValue)
                requestJson.put("password",passwordValue)
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, requestJson,
                    { response ->

                        Log.d("TEST199", response.toString())
                        if (response.toString().contains("token")){
                            val token =response.getString("token")
                            try {
                                val chunks = token.split(".")
                                val decoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Base64.getUrlDecoder()

                                } else {
                                    TODO("VERSION.SDK_INT < O")
                                }
                                val header = String(decoder.decode(chunks[0]))
                                val payload = JSONObject(String(decoder.decode(chunks[1])))
                                val userId = payload.getString("sub")
                                appSettings.putString("TOKEN",token)
                                appSettings.putInt("USER_ID",userId.toInt())
                                Utils.dismiss()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            catch (e:java.lang.Exception){
                                Utils.dismiss()
                                e.printStackTrace()
                            }

                        }
                        else{
                            Utils.dismiss()
                            Toast.makeText(this, response.toString().uppercase(), Toast.LENGTH_SHORT).show()
                        }

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