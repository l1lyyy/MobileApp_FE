package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateBookImportOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_order)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun goToEditActivity(view: View) {
        val intent = Intent(this, CreateBookImportOderEditActivity::class.java)
        startActivity(intent)
    }

    fun goToVerifyActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}