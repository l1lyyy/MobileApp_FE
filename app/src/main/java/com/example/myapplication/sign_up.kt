package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class sign_up : AppCompatActivity() {
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var signup: ImageButton

    private lateinit var postApi: PostApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        username = findViewById(R.id.username_input)
        email = findViewById(R.id.email_input)
        password = findViewById(R.id.password_input)
        confirmPassword = findViewById(R.id.confirm_password_input)
        signup = findViewById(R.id.sign_up_button)

        signup.setOnClickListener {
            Log.d("SignUp", "Sign-up button clicked")
            val username_res = username.text.toString()
            val email_res = email.text.toString()
            val password_res = password.text.toString()
            val confirm_password_res = confirmPassword.text.toString()

            if (validateInput(username_res, email_res, password_res, confirm_password_res)) {
                Log.i("SignUp", "Validation passed")
                val user = User(username_res, email_res, password_res, confirm_password_res, "")
                sendUserData(user)
                Log.i("SignUp", "User data sent: $username_res, $email_res")
            } else {
                Log.e("SignUp", "Validation failed")
                Toast.makeText(this, "Validation failed. Please check your input.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            username.isEmpty() -> {
                Log.e("Validation", "Username is empty")
                false
            }
            email.isEmpty() -> {
                Log.e("Validation", "Email is empty")
                false
            }
            password.isEmpty() -> {
                Log.e("Validation", "Password is empty")
                false
            }
            confirmPassword.isEmpty() -> {
                Log.e("Validation", "Confirm password is empty")
                false
            }
            password != confirmPassword -> {
                Log.e("Validation", "Passwords do not match")
                false
            }
            else -> true
        }
    }

    private fun sendUserData(user: User) {
        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(PostApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        postApi = retrofit.create(PostApi::class.java)

        val call = postApi.registrationUser(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.i("Registration", "Registration successful")
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                    goToSignInActivity()
                } else {
                    Log.e("Registration", "Registration failed: ${response.errorBody()?.string()}")
                    Toast.makeText(applicationContext, "Registration failed. Please try again.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Registration", "Error: ${t.message}")
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun goToSignInActivity(view: View? = null) {
        val intent = Intent(this, sign_in::class.java)
        startActivity(intent)
    }

    fun goToWelcomeActivity() {
        val intent = Intent(this, welcome::class.java)
        startActivity(intent)
    }
}