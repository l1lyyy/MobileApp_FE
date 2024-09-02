package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MonthlyDebtReportEditActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_debt_report_edit)

        // Retrieve the edit type passed through the intent
        val editType = intent.getStringExtra("customer_type") ?: "Default"

        // Use the edit type to customize the activity, for example, changing the header text
        val headerTextView = findViewById<TextView>(R.id.header_text_view)
        headerTextView.text = "$editType"

        // Create a unique SharedPreferences file for each edit type
        sharedPreferences = getSharedPreferences("MonthlyDebtPrefs_$editType", MODE_PRIVATE)
        restoreState()
    }

    override fun onPause() {
        super.onPause()
        saveState()
    }

    private fun saveState() {
        val editor = sharedPreferences.edit()
        val customerIdInput = findViewById<EditText>(R.id.customer_id_input)
        val beginningDebtInput = findViewById<EditText>(R.id.beginning_debt_input)
        val arisingDebtInput = findViewById<EditText>(R.id.arising_debt_input)
        val endingDebtInput = findViewById<EditText>(R.id.ending_debt_input)

        editor.putString("customerIdInput", customerIdInput.text.toString())
        editor.putString("beginningDebtInput", beginningDebtInput.text.toString())
        editor.putString("arisingDebtInput", arisingDebtInput.text.toString())
        editor.putString("endingDebtInput", endingDebtInput.text.toString())
        editor.apply()
    }

    private fun restoreState() {
        val customerIdInput = findViewById<EditText>(R.id.customer_id_input)
        val beginningDebtInput = findViewById<EditText>(R.id.beginning_debt_input)
        val arisingDebtInput = findViewById<EditText>(R.id.arising_debt_input)
        val endingDebtInput = findViewById<EditText>(R.id.ending_debt_input)

        customerIdInput.setText(sharedPreferences.getString("customerIdInput", ""))
        beginningDebtInput.setText(sharedPreferences.getString("beginningDebtInput", ""))
        arisingDebtInput.setText(sharedPreferences.getString("arisingDebtInput", ""))
        endingDebtInput.setText(sharedPreferences.getString("endingDebtInput", ""))
    }

    fun goToDebtReportActivity(view: View) {
        val intent = Intent(this, MonthlyDebtReportActivity::class.java)
        startActivity(intent)
    }
}
