package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.Gson

class sign_up : AppCompatActivity() {
    lateinit var username : EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirm_password: EditText
    //lateinit var signin: TextView
    lateinit var signup: ImageButton

    private lateinit var postApi: PostApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        username = findViewById(R.id.username_input)
        email = findViewById(R.id.email_input)
        password = findViewById(R.id.password_input)
        confirm_password = findViewById(R.id.confirm_password_input)
        signup = findViewById(R.id.sign_up_button)

        signup.setOnClickListener {
            val username_res = username.text.toString()
            val email_res = email.text.toString()
            val password_res = password.text.toString()
            val confirm_password_res = confirm_password.text.toString()
            if(validateInput(username_res,email_res,password_res,confirm_password_res)){
                val user = User(username_res, email_res, password_res,confirm_password_res)
                //sendUserData(user)
                sendUserData(username_res, email_res, password_res,confirm_password_res)
                Log.i("Thong tin dang nhap","username: $username_res, email: $email_res, password: $password_res")
            }
            //to do
            // sau khi goi ham xac nhan thong tin dang ky, chinh sua lai ham xac nhan thong tin
            // dang ky sao cho neu hop le thi goi ham chuyen toi slide tiep theo
        }

//        signin = findViewById(R.id.goToSignIn)
//
//        signin.setOnClickListener {
//            goToSignInActivity()
//        }

    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        // Implement validation logic
        if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword)
        {
            //goToWelcomeActivity()
            return true
        }
        else
        {
            return false
        }
    }

    fun goToSignInActivity(view: View)
    {
        val intent = Intent(this, sign_in::class.java)
        startActivity(intent)
    }

    fun goToWelcomeActivity()
    {
        val intent = Intent(this, welcome::class.java)
        startActivity(intent)
    }

    fun goToSignInActivity()
    {
        val intent = Intent(this, sign_in::class.java)
        startActivity(intent)
    }
//    private fun sendUserData(user: User) {
//        // Khởi tạo Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl(PostApi.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        postApi = retrofit.create(PostApi::class.java)
//
//        val call = postApi.registrationUser(user)
//        call.enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                Log.i("Thong tin dang nhap","gui thanh cong")
//                if (response.isSuccessful) {
//                    Log.i("Registration", "Registration successful")
//                    // Navigate to the next screen upon successful registration
//                    goToSignInActivity()
//                } else {
//                    Log.e("Registration", "Registration failed")
//                    goToWelcomeActivity()
//                    // Handle registration failure
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.e("Registration", "Error: ${t.message}")
//                // Handle failure to connect to server
//            }
//        })
//    }
    private fun sendUserData(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)

        val user = User(username = username, email = email,password = password, confirmPassword =confirmPassword)

        val gson = Gson()
        val json = gson.toJson(user)
        Log.d("User JSON",json)

        val call = postApi.registrationUser(user)

    call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.i("Thong tin dang nhap","gui thanh cong")
                if (response.isSuccessful) {
                    Log.i("Registration", "Registration successful")
                    // Navigate to the next screen upon successful registration
                    goToSignInActivity()
                } else {
                    Log.e("Registration", "Registration failed")
                    goToWelcomeActivity()
                    // Handle registration failure
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Registration", "Error: ${t.message}")
                // Handle failure to connect to server
            }
        })
    }
}