package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
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
    private lateinit var addSlotButtonContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_order)

        // Initialize the EditText for date input
        dateInput = findViewById(R.id.date_input)

        // Initialize the container for the add slot button
        addSlotButtonContainer = findViewById(R.id.add_slot_button_container)

        // Set up the calendar button click listener
        findViewById<View>(R.id.calendar_button).setOnClickListener {
            showDatePickerDialog()
        }

        // Set up the add slot button click listener
        findViewById<View>(R.id.add_slot).setOnClickListener {
            onAddSlotButtonClicked()
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

    private fun onAddSlotButtonClicked() {
        // Adjust the margin of the "Add slot" button and text
        val layoutParams = addSlotButtonContainer.layoutParams as RelativeLayout.LayoutParams
        layoutParams.topMargin += dpToPx(100)
        addSlotButtonContainer.layoutParams = layoutParams
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun goToEditActivity(view: View) {
        val intent = Intent(this, CreateBookImportOderEditActivity::class.java)
        startActivity(intent)
    }
}