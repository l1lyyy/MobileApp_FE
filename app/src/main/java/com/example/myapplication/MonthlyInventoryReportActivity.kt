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
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MonthlyInventoryReportActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var date: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

    // Danh sách các `TextView` và `ImageButton` cho các slot
    private lateinit var bookIds: List<TextView>
    private lateinit var bookNames: List<TextView>
    private lateinit var begins: List<TextView>
    private lateinit var arises: List<TextView>
    private lateinit var ends: List<TextView>
    private lateinit var editButtons: List<ImageButton>
    private var currentSlotIndex: Int = -1  // Theo dõi slot hiện tại

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_inventory_report)

        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""

        bookIds = listOf(
            findViewById(R.id.book_id_1), findViewById(R.id.book_id_2), findViewById(R.id.book_id_3),
            findViewById(R.id.book_id_4), findViewById(R.id.book_id_5), findViewById(R.id.book_id_6),
            findViewById(R.id.book_id_7), findViewById(R.id.book_id_8), findViewById(R.id.book_id_9),
            findViewById(R.id.book_id_10)
        )

        bookNames = listOf(
            findViewById(R.id.book_name_1), findViewById(R.id.book_name_2), findViewById(R.id.book_name_3),
            findViewById(R.id.book_name_4), findViewById(R.id.book_name_5), findViewById(R.id.book_name_6),
            findViewById(R.id.book_name_7), findViewById(R.id.book_name_8), findViewById(R.id.book_name_9),
            findViewById(R.id.book_name_10)
        )

        begins = listOf(
            findViewById(R.id.begin_1), findViewById(R.id.begin_2), findViewById(R.id.begin_3),
            findViewById(R.id.begin_4), findViewById(R.id.begin_5), findViewById(R.id.begin_6),
            findViewById(R.id.begin_7), findViewById(R.id.begin_8), findViewById(R.id.begin_9),
            findViewById(R.id.begin_10)
        )

        arises = listOf(
            findViewById(R.id.arise_1), findViewById(R.id.arise_2), findViewById(R.id.arise_3),
            findViewById(R.id.arise_4), findViewById(R.id.arise_5), findViewById(R.id.arise_6),
            findViewById(R.id.arise_7), findViewById(R.id.arise_8), findViewById(R.id.arise_9),
            findViewById(R.id.arise_10)
        )

        ends = listOf(
            findViewById(R.id.end_1), findViewById(R.id.end_2), findViewById(R.id.end_3),
            findViewById(R.id.end_4), findViewById(R.id.end_5), findViewById(R.id.end_6),
            findViewById(R.id.end_7), findViewById(R.id.end_8), findViewById(R.id.end_9),
            findViewById(R.id.end_10)
        )

        editButtons = listOf(
            findViewById(R.id.edit_button_1), findViewById(R.id.edit_button_2), findViewById(R.id.edit_button_3),
            findViewById(R.id.edit_button_4), findViewById(R.id.edit_button_5), findViewById(R.id.edit_button_6),
            findViewById(R.id.edit_button_7), findViewById(R.id.edit_button_8), findViewById(R.id.edit_button_9),
            findViewById(R.id.edit_button_10)
        )

        // Add listeners for edit buttons (for each slot)
        for (i in editButtons.indices) {
            editButtons[i].setOnClickListener {
                currentSlotIndex = i  // Cập nhật slot hiện tại
                saveStateToPreferences()
                val intent = Intent(this, MonthlyInventoryReportEditActivity::class.java)
                intent.putExtra("index", i)
                resultLauncher.launch(intent)
            }
        }

        // Thiết lập resultLauncher để nhận dữ liệu từ MonthlyInventoryReportEditActivity
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && currentSlotIndex != -1) {
                val data = result.data
                val id = data?.getStringExtra("book_id")
                val name = data?.getStringExtra("book_name")
                val begin = data?.getStringExtra("begin_inventory")
                val arise = data?.getStringExtra("arise_inventory")
                val end = data?.getStringExtra("end_inventory")

                // Cập nhật các `TextView` trong slot hiện tại
                bookIds[currentSlotIndex].text = "$id\n"
                bookNames[currentSlotIndex].text = "$name\n"
                begins[currentSlotIndex].text = "$begin\n"
                arises[currentSlotIndex].text = "$arise\n"
                ends[currentSlotIndex].text = "$end\n"

                Log.d("MonthlyInventoryReport", "Slot $currentSlotIndex - Book ID: $id, Book Name: $name, Begin: $begin, Arise: $arise, End: $end")
            }
        }

        // Initialize the EditText for date input
        date = findViewById(R.id.date_input)
        confirm_button =findViewById(R.id.check_square_button)
        confirm_button.setOnClickListener {
            val date_res = formatDateString(date.text.toString())
            //val inventoryReports = mutableListOf<InventoryReport>()

            for (i in bookIds.indices) {
                val bookId = bookIds[i].text.toString().trim()
                val bookName = bookNames[i].text.toString().trim()
                val begin = begins[i].text.toString().trim().toIntOrNull() ?: 0
                val arise = arises[i].text.toString().trim().toIntOrNull() ?: 0
                val end = ends[i].text.toString().trim().toIntOrNull() ?: 0

                if (bookId.isNotEmpty() && bookName.isNotEmpty() && begin > 0 && arise > 0 && end > 0) {
                    //inventoryReports.add(InventoryReport(date_res,bookId, bookName, begin, arise, end ))
                }
            }

            //sendInventoryReport(inventoryReports)
        }

        // Restore the state from SharedPreferences
        restoreStateFromPreferences()

        // Set up Calendar Button
        findViewById<ImageButton>(R.id.calendar_button).setOnClickListener {
            showDatePickerDialog()
        }
    }


    fun formatDateString(dateString: String): String {
        // Split the date string by "/"
        val parts = dateString.split("/")

        // Extract day, month, and year
        val day = parts[0].padStart(2, '0')   // Ensures day is two digits
        val month = parts[1].padStart(2, '0') // Ensures month is two digits
        val year = parts[2]                   // Year remains as is

        // Return the formatted date in "yyyy-mm-dd" format
        return "$year-$month-$day"
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
            date.setText(formattedDate)
        }, year, month, day)

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("InventoryReportPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("dateInputText", date.text.toString())

        for (i in bookIds.indices) {
            editor.putString("bookId_$i", bookIds[i].text.toString())
            editor.putString("bookName_$i", bookNames[i].text.toString())
            editor.putString("begin_$i", begins[i].text.toString())
            editor.putString("arise_$i", arises[i].text.toString())
            editor.putString("end_$i", ends[i].text.toString())
        }

        editor.apply()
    }

    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("InventoryReportPrefs", MODE_PRIVATE)
        date.setText(sharedPreferences.getString("dateInputText", ""))

        for (i in bookIds.indices) {
            bookIds[i].text = sharedPreferences.getString("bookId_$i", "ID")
            bookNames[i].text = sharedPreferences.getString("bookName_$i", "Book " + (i+1).toString())
            begins[i].text = sharedPreferences.getString("begin_$i", "Begin")
            arises[i].text = sharedPreferences.getString("arise_$i", "Arise")
            ends[i].text = sharedPreferences.getString("end_$i", "End")
        }
    }

    private fun clearPreferences() {
        val sharedPreferences = getSharedPreferences("InventoryReportPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    override fun onBackPressed() {
        clearAllPreferences() // Xóa tất cả các trạng thái
        super.onBackPressed() // Quay lại dashboard
    }

    private fun clearAllPreferences() {
        // Xóa trạng thái của InventoryReportActivity
        val sharedPreferencesOrder = getSharedPreferences("InventoryReportPrefs", MODE_PRIVATE)
        sharedPreferencesOrder.edit().clear().apply()

        // Xóa trạng thái của từng InventoryReportEditActivity
        for (i in bookIds.indices) {
            val sharedPreferencesEdit = getSharedPreferences("InventoryReportEditPrefs_$i", MODE_PRIVATE)
            sharedPreferencesEdit.edit().clear().apply()
        }
    }

    fun goToMonthlyReportActivity(view: View) {
        // Save the current state before switching activities
        clearAllPreferences()
        val intent = Intent(this, MonthlyReportActivity::class.java)
        startActivity(intent)
    }

}