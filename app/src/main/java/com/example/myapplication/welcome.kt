package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.content.Intent

class welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
    }

    fun goToSignUpActivity(view: View) {
        val intent = Intent(this, sign_up::class.java)
        startActivity(intent)
    }

    fun goToSignInActivity(view: View) {
        val intent = Intent(this, sign_in::class.java)
        startActivity(intent)
    }

    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}
