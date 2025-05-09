package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class sign_in : AppCompatActivity() {
    lateinit var username_email : EditText
    lateinit var password: EditText
    lateinit var signin: ImageButton
    private lateinit var postApi: PostApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        username_email = findViewById(R.id.user_input)
        password = findViewById(R.id.user_password_input)
        signin = findViewById(R.id.sign_in_button)
        signin.setOnClickListener {
            val username_email_res =  username_email.text.toString()
            val password_res = password.text.toString()
            signIn(username_email_res,password_res)
        }
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

    fun goToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun validateInput(username: String, email: String, password: String): Boolean {
        // Implement validation logic
        if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
        {
            return true
        }
        else
        {
            return false
        }
    }

    fun gotoCreateSalesInvoiceActivity()
    {
        val intent = Intent(this, CreateSalesInvoiceActivity::class.java)
        startActivity(intent)
    }

    private fun signIn(username: String,password: String) {
        val builder = Retrofit.Builder()
            .baseUrl(PostApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)

        val signIn = Signin(username,password)

        val call = postApi.signinUser(signIn)

        call.enqueue(object : Callback<Token>
        {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    response.body()?.let { tokenResponse ->
                        val tokenValue = tokenResponse.token
                        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        val prefSigninEdit = preferences?.edit()
                        prefSigninEdit?.putBoolean("signed in", true)
                        prefSigninEdit?.putString("token", tokenValue)
                        prefSigninEdit?.apply()
                        println("Retrieved Token: $tokenValue")
                        Toast.makeText(this@sign_in, "Login successfully", Toast.LENGTH_SHORT).show()
                        goToDashboardActivity()
                    }
                } else {
                    Toast.makeText(this@sign_in, "Login failed, wrong password or account",Toast.LENGTH_LONG).show()

                }
            }
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(this@sign_in, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}