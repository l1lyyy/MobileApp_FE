package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.Gson
import android.util.Log
import java.util.Calendar

class CreateBookImportOrderActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var date: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

    // Danh sách các `TextView` và `ImageButton` cho các slot
    private lateinit var bookIds: List<TextView>
    private lateinit var bookNames: List<TextView>
    private lateinit var authors: List<TextView>
    private lateinit var amounts: List<TextView>
    private lateinit var editButtons: List<ImageButton>
    private var currentSlotIndex: Int = -1  // Theo dõi slot hiện tại
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_order)

        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""

        // Initialize danh sách các `TextView` và `ImageButton`
        bookIds = listOf(
            findViewById(R.id.book_ID_1), findViewById(R.id.book_ID_2), findViewById(R.id.book_ID_3),
            findViewById(R.id.book_ID_4), findViewById(R.id.book_ID_5), findViewById(R.id.book_ID_6),
            findViewById(R.id.book_ID_7), findViewById(R.id.book_ID_8), findViewById(R.id.book_ID_9),
            findViewById(R.id.book_ID_10)
        )

        bookNames = listOf(
            findViewById(R.id.book_name_1), findViewById(R.id.book_name_2), findViewById(R.id.book_name_3),
            findViewById(R.id.book_name_4), findViewById(R.id.book_name_5), findViewById(R.id.book_name_6),
            findViewById(R.id.book_name_7), findViewById(R.id.book_name_8), findViewById(R.id.book_name_9),
            findViewById(R.id.book_name_10)
        )

        authors = listOf(
            findViewById(R.id.author_1), findViewById(R.id.author_2), findViewById(R.id.author_3),
            findViewById(R.id.author_4), findViewById(R.id.author_5), findViewById(R.id.author_6),
            findViewById(R.id.author_7), findViewById(R.id.author_8), findViewById(R.id.author_9),
            findViewById(R.id.author_10)
        )

        amounts = listOf(
            findViewById(R.id.amount_1), findViewById(R.id.amount_2), findViewById(R.id.amount_3),
            findViewById(R.id.amount_4), findViewById(R.id.amount_5), findViewById(R.id.amount_6),
            findViewById(R.id.amount_7), findViewById(R.id.amount_8), findViewById(R.id.amount_9),
            findViewById(R.id.amount_10)
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
                saveStateToPreferences() // Lưu trạng thái của CreateBookImportOrderActivity
                val intent = Intent(this, CreateBookImportOderEditActivity::class.java)
                intent.putExtra("index", i) // Truyền index cho CreateBookImportOderEditActivity
                resultLauncher.launch(intent)
            }
        }

        // Thiết lập resultLauncher để nhận dữ liệu từ CreateBookImportOderEditActivity
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val id = data?.getStringExtra("bookid")
                val name = data?.getStringExtra("bookname")
                val author = data?.getStringExtra("author")
                val amount = data?.getStringExtra("amount")

                val index = data?.getIntExtra("index", -1) ?: -1 // Lấy index từ kết quả trả về

                if (index != -1) {
                    // Cập nhật các `TextView` trong slot hiện tại
                    bookIds[index].text = "$id\n"
                    bookNames[index].text = "$name\n"
                    authors[index].text = "$author\n"
                    amounts[index].text = "$amount\n"

                    Log.d("CreateBookImportOrder", "Slot $index - Book ID: $id, Book Name: $name, Author: $author, Amount: $amount")
                }
            }
        }

        date = findViewById(R.id.date_input)
        confirm_button =findViewById(R.id.check_square_button)

        confirm_button.setOnClickListener {
            val date_res = formatDateString(date.text.toString())
            val importOrders = mutableListOf<ImportOrder>()

            for (i in bookIds.indices) {
                val bookId = bookIds[i].text.toString().trim()
                val bookName = bookNames[i].text.toString().trim()
                val author = authors[i].text.toString().trim()
                val amount = amounts[i].text.toString().trim().toIntOrNull() ?: 0

                if (bookId.isNotEmpty() && bookName.isNotEmpty() && author.isNotEmpty() && amount > 0) {
                    importOrders.add(ImportOrder(bookId, bookName, author, amount, date_res))
                }
            }

            sendImportOrder(ImportOrderRequest(date_res, importOrders))
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

    private fun sendImportOrder(importOrderRequest: ImportOrderRequest) {
        if (importOrderRequest.details.isEmpty()) {
            Toast.makeText(this, "No valid data to send", Toast.LENGTH_SHORT).show()
            return
        }

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder()
            .baseUrl(PostApi.IMPORT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())

        val retrofit = builder.build()
        postApi = retrofit.create(PostApi::class.java)

        val gson = Gson()
        val json = gson.toJson(importOrderRequest)
        Log.d("importOrderRequest JSON", json)

        val call = postApi.sendImportOrder("Token $token", importOrderRequest)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateBookImportOrderActivity, "Import orders sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateBookImportOrderActivity, "Failed to send import orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CreateBookImportOrderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save the current state of date input
        editor.putString("dateInputText", date.text.toString())

        // Save the state of book details for each slot
        for (i in bookIds.indices) {
            editor.putString("bookId_$i", bookIds[i].text.toString())
            editor.putString("bookName_$i", bookNames[i].text.toString())
            editor.putString("author_$i", authors[i].text.toString())
            editor.putString("amount_$i", amounts[i].text.toString())
        }

        editor.apply() // Save all changes
    }


    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)

        // Khôi phục trạng thái
        date.setText(sharedPreferences.getString("dateInputText", ""))

        // Khôi phục trạng thái các trường book, author, amount từ CreateBookImportOderEditActivity
        for (i in bookIds.indices) {
            bookIds[i].text = sharedPreferences.getString("bookId_$i", "ID")
            bookNames[i].text = sharedPreferences.getString("bookName_$i", "Book " + (i+1).toString())
            authors[i].text = sharedPreferences.getString("author_$i", "Author")
            amounts[i].text = sharedPreferences.getString("amount_$i", "Amount")
        }
    }

    private fun clearPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    override fun onBackPressed() {
        clearAllPreferences() // Xóa tất cả các trạng thái
        super.onBackPressed() // Quay lại dashboard
    }

    private fun clearAllPreferences() {
        // Xóa trạng thái của CreateBookImportOrderActivity
        val sharedPreferencesOrder = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        sharedPreferencesOrder.edit().clear().apply()

        // Xóa trạng thái của từng CreateBookImportOderEditActivity
        for (i in bookIds.indices) {
            val sharedPreferencesEdit = getSharedPreferences("CreateBookImportOrderEditPrefs_$i", MODE_PRIVATE)
            sharedPreferencesEdit.edit().clear().apply()
        }
    }

    fun goToDashboardActivity(view: View) {
        clearAllPreferences() // Xóa tất cả các trạng thái trước khi chuyển sang dashboard
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun goToEditActivity(editType: String) {
        saveStateToPreferences()
        val intent = Intent(this, CreateBookImportOderEditActivity::class.java)
        intent.putExtra("book_type", editType)
        startActivity(intent)
    }

}

