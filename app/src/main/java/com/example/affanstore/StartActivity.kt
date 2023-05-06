package com.example.affanstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class StartActivity : AppCompatActivity() {

    lateinit var loginBtn:MaterialButton
    lateinit var signupBtn:MaterialButton
    lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
         appSettings = AppSettings(this)
         if (appSettings.getString("TOKEN")!!.isNotEmpty()){
             startActivity(Intent(this,MainActivity::class.java))
             finish()
         }
        loginBtn = findViewById(R.id.login_btn)

        signupBtn = findViewById(R.id.sign_up_btn)

        loginBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}