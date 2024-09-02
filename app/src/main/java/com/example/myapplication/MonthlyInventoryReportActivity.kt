package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MonthlyInventoryReportActivity : AppCompatActivity() {
    private lateinit var dateInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_inventory_report)

        // Initialize the EditText for date input
        dateInput = findViewById(R.id.date_input)

        // Restore the state from SharedPreferences
        restoreStateFromPreferences()

        // Set up the navigation to the edit activities
        findViewById<ImageButton>(R.id.edit_button_1).setOnClickListener {
            goToEditActivity("book 1")
        }

        findViewById<ImageButton>(R.id.edit_button_2).setOnClickListener {
            goToEditActivity("book 2")
        }

        findViewById<ImageButton>(R.id.edit_button_3).setOnClickListener {
            goToEditActivity("book 3")
        }

        findViewById<ImageButton>(R.id.edit_button_4).setOnClickListener {
            goToEditActivity("book 4")
        }

        findViewById<ImageButton>(R.id.edit_button_5).setOnClickListener {
            goToEditActivity("book 5")
        }

        findViewById<ImageButton>(R.id.edit_button_6).setOnClickListener {
            goToEditActivity("book 6")
        }

        findViewById<ImageButton>(R.id.edit_button_7).setOnClickListener {
            goToEditActivity("book 7")
        }

        findViewById<ImageButton>(R.id.edit_button_8).setOnClickListener {
            goToEditActivity("book 8")
        }

        findViewById<ImageButton>(R.id.edit_button_9).setOnClickListener {
            goToEditActivity("book 9")
        }

        findViewById<ImageButton>(R.id.edit_button_10).setOnClickListener {
            goToEditActivity("book 10")
        }
    }

    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save the current state
        editor.putString("dateInputText", dateInput.text.toString())
        editor.apply()
    }

    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)

        // Restore the state
        dateInput.setText(sharedPreferences.getString("dateInputText", ""))
    }

    private fun clearPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun goToMonthlyReportActivity(view: View) {
        // Save the current state before switching activities
        clearPreferences()
        val intent = Intent(this, MonthlyReportActivity::class.java)
        startActivity(intent)
    }

    private fun goToEditActivity(editType: String) {
        // Navigate to EditActivity with the type of edit
        saveStateToPreferences()
        val intent = Intent(this, MonthlyInventoryReportEditActivity::class.java)
        intent.putExtra("book_type", editType)
        startActivity(intent)
    }

}