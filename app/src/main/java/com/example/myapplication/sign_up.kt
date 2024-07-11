package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySignUpBinding

class sign_up : AppCompatActivity() {
    lateinit var username : EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var signup: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        username = findViewById(R.id.signup_username_input)
        email = findViewById(R.id.signup_email_input)
        password = findViewById(R.id.signup_password_input)
        confirmPassword = findViewById(R.id.signup_confirm_password_input)
        signup = findViewById(R.id.signup_button)

        signup.setOnClickListener {
            val username_res = username.text.toString()
            val email_res = username.text.toString()
            val password_res = password.text.toString()
            val confirm_password_res = confirmPassword.text.toString()
            validateInput(username_res,email_res,password_res,confirm_password_res)
            //to do
            // sau khi goi ham xac nhan thong tin dang ky, chinh sua lai ham xac nhan thong tin
            // dang ky sao cho neu hop le thi goi ham chuyen toi slide tiep theo
            Log.i("Thong tin dang nhap","username: $username_res, email: $email_res, password: $password_res")
        }

    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        // Implement validation logic
        if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword)
        {
            goToWelcomeActivity()
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
}