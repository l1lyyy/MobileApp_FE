package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class sign_in : AppCompatActivity() {
    lateinit var username_email : EditText
    lateinit var password: EditText
    lateinit var signin: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        username_email = findViewById(R.id.signin_username_email_input)
        password = findViewById(R.id.signin_password_input)

//        signin.setOnClickListener {
//            val username_email_res =  username_email.text.toString()
//            val password_res = password.text.toString()

//        }
    }
    fun goToWelcomeActivity(view: View)
    {
        val intent = Intent(this, welcome::class.java)
        startActivity(intent)
    }

    fun goToSignUpActivity(view: View)
    {
        val intent = Intent(this, sign_up::class.java)
        startActivity(intent)
    }

    fun goToDashboardActivity(view: View)
    {
        val intent = Intent(this, welcome::class.java)
        startActivity(intent)
    }
}