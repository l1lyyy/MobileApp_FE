package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

class MonthlyDebtReportEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_debt_report_edit)

        // Retrieve the edit type passed through the intent
        val editType = intent.getStringExtra("customer_type")

        // Use the edit type to customize the activity, for example, changing the header text
        val headerTextView = findViewById<TextView>(R.id.header_text_view)
        headerTextView.text = "$editType"
    }

    fun goToDebtReportActivity(view: View) {
        val intent = Intent(this, MonthlyDebtReportActivity::class.java)
        startActivity(intent)
    }

}