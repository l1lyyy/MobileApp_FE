package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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

class CreateSalesInvoiceActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var customer_name: EditText
    private lateinit var date: EditText
    private lateinit var create: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sales_invoice)

        // Initialize SharedPreferences and token
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""

        // Initialize UI components
        customer_name = findViewById(R.id.customer_name_input)
        date = findViewById(R.id.date_input)
        create = findViewById(R.id.check_square_button)

        // Initialize Retrofit and API interface
        val retrofit = Retrofit.Builder()
            .baseUrl(PostApi.INVOICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        postApi = retrofit.create(PostApi::class.java)

        // Restore state from SharedPreferences
        restoreStateFromPreferences()

        // Set up result launcher
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val bookname = data?.getStringExtra("bookname") ?: ""
                val amount = data?.getStringExtra("amount") ?: "0"
                val priceString = data?.getStringExtra("price") ?: "0.0"
                val price = priceString.toFloatOrNull() ?: 0.0f
                findViewById<TextView>(R.id.book_name_1).text = "$bookname\n"
                findViewById<TextView>(R.id.amount_1).text = "$amount\n"
                findViewById<TextView>(R.id.price_1).text = "$price\n"
            }
        }

        // Set up Edit buttons
        setupEditButtons()

        // Set up Calendar Button
        findViewById<ImageButton>(R.id.calendar_button).setOnClickListener {
            showDatePickerDialog()
        }

        // Set up Create button
        create.setOnClickListener {
            val customerName = customer_name.text.toString()
            val dateInput = date.text.toString()
            val bookName = findViewById<TextView>(R.id.book_name_1).text.toString()
            val amount = findViewById<TextView>(R.id.amount_1).text.toString().toIntOrNull() ?: 0
            val price = findViewById<TextView>(R.id.price_1).text.toString().toFloatOrNull() ?: 0.0f
            sendSalesInvoice(customerName, dateInput, bookName, "null", amount, price, token)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                date.setText(dateFormat.format(selectedDate.time))
            },
            year, month, day
        ).show()
    }

    private fun sendSalesInvoice(customerName: String, date: String, bookName: String, category: String, amount: Int, price: Float, token: String) {
        val invoice = Salesinvoice("", customerName, date, bookName, category, amount, price)
        postApi.sendSalesInvoice("Token $token", invoice).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateSalesInvoiceActivity, "Invoice sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateSalesInvoiceActivity, "Failed to send invoice", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CreateSalesInvoiceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoicePrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save the current state
        editor.putString("customerName", customer_name.text.toString())
        editor.putString("dateInputText", date.text.toString())
        editor.apply()
    }

    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoicePrefs", MODE_PRIVATE)

        // Restore the state
        customer_name.setText(sharedPreferences.getString("customerName", ""))
        date.setText(sharedPreferences.getString("dateInputText", ""))
    }

    private fun clearPreferences() {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoicePrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun goToDashboardActivity(view: View) {
        clearPreferences()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun goToEditActivity(bookType: String) {
        saveStateToPreferences()
        val intent = Intent(this, CreateSalesInvoiceEditActivity::class.java)
        intent.putExtra("book_type", bookType)
        resultLauncher.launch(intent)
    }

    private fun setupEditButtons() {
        val editButtonIds = listOf(
            R.id.edit_button_1, R.id.edit_button_2, R.id.edit_button_3,
            R.id.edit_button_4, R.id.edit_button_5, R.id.edit_button_6,
            R.id.edit_button_7, R.id.edit_button_8, R.id.edit_button_9, R.id.edit_button_10
        )
        val bookTypes = listOf(
            "book 1", "book 2", "book 3", "book 4", "book 5",
            "book 6", "book 7", "book 8", "book 9", "book 10"
        )

        for (i in editButtonIds.indices) {
            findViewById<ImageButton>(editButtonIds[i]).setOnClickListener {
                goToEditActivity(bookTypes[i])
            }
        }
    }
}
