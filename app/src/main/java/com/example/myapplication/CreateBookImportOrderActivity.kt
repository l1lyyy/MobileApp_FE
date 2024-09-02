package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.widget.ImageButton
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class CreateBookImportOrderActivity : AppCompatActivity() {

    private lateinit var dateInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_order)

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

        // Set up the calendar button click listener
        findViewById<View>(R.id.calendar_button).setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date and set it to the EditText
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            dateInput.setText(formattedDate)
        }, year, month, day)

        // Show the DatePickerDialog
        datePickerDialog.show()
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

    fun goToDashboardActivity(view: View) {
        // Save the current state before switching activities
        clearPreferences()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun goToEditActivity(editType: String) {
        // Navigate to EditActivity with the type of edit
        saveStateToPreferences()
        val intent = Intent(this, CreateBookImportOderEditActivity::class.java)
        intent.putExtra("book_type", editType)
        startActivity(intent)
    }


}
