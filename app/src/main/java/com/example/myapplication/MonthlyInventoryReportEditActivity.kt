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

class MonthlyInventoryReportEditActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_inventory_report_edit)

        // Retrieve the edit type passed through the intent
        editType = intent.getStringExtra("book_type") ?: "default"

        // Use the edit type to customize the activity, for example, changing the header text
        val headerTextView = findViewById<TextView>(R.id.header_text_view)
        headerTextView.text = "Edit $editType"

        sharedPreferences = getSharedPreferences("MonthlyInventoryPrefs_$editType", MODE_PRIVATE)
        restoreState()
    }

    override fun onPause() {
        super.onPause()
        saveState()
    }

    private fun saveState() {
        val editor = sharedPreferences.edit()
        val bookIdInput = findViewById<EditText>(R.id.book_id_input)
        val beginningInventoryInput = findViewById<EditText>(R.id.beginning_inventory_input)
        val arisingInventoryInput = findViewById<EditText>(R.id.arising_inventory_input)
        val endingInventoryInput = findViewById<EditText>(R.id.ending_inventory_input)

        editor.putString("bookIdInput", bookIdInput.text.toString())
        editor.putString("beginningInventoryInput", beginningInventoryInput.text.toString())
        editor.putString("arisingInventoryInput", arisingInventoryInput.text.toString())
        editor.putString("endingInventoryInput", endingInventoryInput.text.toString())
        editor.apply()
    }

    private fun restoreState() {
        val bookIdInput = findViewById<EditText>(R.id.book_id_input)
        val beginningInventoryInput = findViewById<EditText>(R.id.beginning_inventory_input)
        val arisingInventoryInput = findViewById<EditText>(R.id.arising_inventory_input)
        val endingInventoryInput = findViewById<EditText>(R.id.ending_inventory_input)

        bookIdInput.setText(sharedPreferences.getString("bookIdInput", ""))
        beginningInventoryInput.setText(sharedPreferences.getString("beginningInventoryInput", ""))
        arisingInventoryInput.setText(sharedPreferences.getString("arisingInventoryInput", ""))
        endingInventoryInput.setText(sharedPreferences.getString("endingInventoryInput", ""))
    }

    fun goToInventoryReportActivity(view: View) {
        val intent = Intent(this, MonthlyInventoryReportActivity::class.java)
        startActivity(intent)
    }
}
