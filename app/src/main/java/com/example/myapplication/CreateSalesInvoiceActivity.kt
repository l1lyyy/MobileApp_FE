package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CreateSalesInvoiceActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var customer_id: EditText
    lateinit var customer_name: EditText
    lateinit var date: EditText
    lateinit var check_button: ImageButton
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

    // Danh sách các `TextView` và `ImageButton` cho các slot
    private lateinit var bookIds: List<TextView>
    private lateinit var bookNames: List<TextView>
    private lateinit var amounts: List<TextView>
    private lateinit var prices: List<TextView>
    private lateinit var editButtons: List<ImageButton>
    private var currentSlotIndex: Int = -1  // Theo dõi slot hiện tại

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sales_invoice)

        // Initialize SharedPreferences and token
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""

        // Initialize danh sách các `TextView` và `ImageButton`
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

        amounts = listOf(
            findViewById(R.id.amount_1), findViewById(R.id.amount_2), findViewById(R.id.amount_3),
            findViewById(R.id.amount_4), findViewById(R.id.amount_5), findViewById(R.id.amount_6),
            findViewById(R.id.amount_7), findViewById(R.id.amount_8), findViewById(R.id.amount_9),
            findViewById(R.id.amount_10)
        )

        prices = listOf(
            findViewById(R.id.price_1), findViewById(R.id.price_2), findViewById(R.id.price_3),
            findViewById(R.id.price_4), findViewById(R.id.price_5), findViewById(R.id.price_6),
            findViewById(R.id.price_7), findViewById(R.id.price_8), findViewById(R.id.price_9),
            findViewById(R.id.price_10)
        )

        editButtons = listOf(
            findViewById(R.id.edit_button_1), findViewById(R.id.edit_button_2), findViewById(R.id.edit_button_3),
            findViewById(R.id.edit_button_4), findViewById(R.id.edit_button_5), findViewById(R.id.edit_button_6),
            findViewById(R.id.edit_button_7), findViewById(R.id.edit_button_8), findViewById(R.id.edit_button_9),
            findViewById(R.id.edit_button_10)
        )

        // Add listeners for edit buttons (for each slot)
        val bookTypes = listOf(
            "book 1", "book 2", "book 3", "book 4", "book 5",
            "book 6", "book 7", "book 8", "book 9", "book 10"
        )

        for (i in editButtons.indices) {
            editButtons[i].setOnClickListener {
                currentSlotIndex = i  // Cập nhật slot hiện tại
                saveStateToPreferences()
                val intent = Intent(this, CreateSalesInvoiceEditActivity::class.java)
                intent.putExtra("book_type", bookTypes[i])
                resultLauncher.launch(intent)
            }
        }

        // Thiết lập resultLauncher để nhận dữ liệu từ CreateBookImportOderEditActivity
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && currentSlotIndex != -1) {
                val data = result.data
                val id = data?.getStringExtra("bookid")
                val name = data?.getStringExtra("bookname")
                val amount = data?.getStringExtra("amount")
                val price = data?.getStringExtra("price")

                // Cập nhật các `TextView` trong slot hiện tại
                bookIds[currentSlotIndex].text = "$id\n"
                bookNames[currentSlotIndex].text = "$name\n"
                amounts[currentSlotIndex].text = "$amount\n"
                prices[currentSlotIndex].text = "$price\n"

                Log.d("CreateBookImportOrder", "Slot $currentSlotIndex - Book ID: $id, Book Name: $name, Amount: $amount, Price: $price")
            }
        }

        // Initialize UI components
        check_button = findViewById(R.id.check_id_button)
        customer_id = findViewById(R.id.customer_id_input)
        customer_name = findViewById(R.id.customer_name_input)
        date = findViewById(R.id.date_input)
        confirm_button =findViewById(R.id.check_square_button)

        // solve
        check_button.setOnClickListener {
            val customer_id_res = customer_id.text.toString()
            sendId(customer_id_res)
        }

        confirm_button.setOnClickListener {
            val customer_id = customer_id.text.toString()
            val customer_name = customer_name.text.toString()
            val date_res = formatDateString(date.text.toString())
            val salesInvoices = mutableListOf<Salesinvoice>()

            for (i in bookIds.indices) {
                val bookId = bookIds[i].text.toString().trim()
                val bookName = bookNames[i].text.toString().trim()
                val amount = amounts[i].text.toString().trim().toIntOrNull() ?: 0
                val price = prices[i].text.toString().trim().toIntOrNull() ?: 0

                if (bookId.isNotEmpty() && bookName.isNotEmpty() && amount > 0 && price > 0) {
                    salesInvoices.add(Salesinvoice(customer_id,customer_name,date_res,bookId,bookName,amount,price))
                }
            }
            sendSalesInvoice(salesInvoices)
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

    // solve
    private fun sendId(id: String) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.CHECK_CUSTOMER_ID_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)

        val id_search = CustomerId(id)
        val gson = Gson()
        val json = gson.toJson(id_search)
        Log.d("customer id JSON", json)

        val call = postApi.sendCustomerId("Token $token", id_search)

        call.enqueue(object : Callback<CustomerInfo> {
            override fun onResponse(call: Call<CustomerInfo>, response: Response<CustomerInfo>) {
                if (response.isSuccessful) {
                    response.body()?.let { CustomerInfo ->
                        // Gán giá trị vào EditText
                        customer_name.setText(CustomerInfo.customerName)
                    }
                    Toast.makeText(
                        this@CreateSalesInvoiceActivity,
                        "customer ID check successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(
                        this@CreateSalesInvoiceActivity,
                        "Failed to check customer ID",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CustomerInfo>, t: Throwable) {
                Toast.makeText(
                    this@CreateSalesInvoiceActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun sendSalesInvoice(salesinvoice: List<Salesinvoice>)
    {
        if(salesinvoice.isEmpty())
        {
            Toast.makeText(this, "No valid data to send", Toast.LENGTH_SHORT).show()
            return
        }

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder()
            .baseUrl(PostApi.INVOICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())

        val retrofit = builder.build()
        postApi = retrofit.create(PostApi::class.java)

        val gson = Gson()
        val json = gson.toJson(salesinvoice)
        Log.d("importOrders JSON", json)

        val call = postApi.sendSalesInvoice("Token $token",salesinvoice)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateSalesInvoiceActivity, "sales invoice sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateSalesInvoiceActivity, "Failed to send sales invoice", Toast.LENGTH_SHORT).show()
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
        editor.putString("customerId", customer_id.text.toString())
        editor.putString("customerName", customer_name.text.toString())
        editor.putString("dateInputText", date.text.toString())
        editor.apply()
    }

    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoicePrefs", MODE_PRIVATE)

        // Restore the state
        customer_id.setText(sharedPreferences.getString("customerId", ""))
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
}