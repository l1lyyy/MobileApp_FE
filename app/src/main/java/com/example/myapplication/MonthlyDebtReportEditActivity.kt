package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthlyDebtReportEditActivity : AppCompatActivity() {
    lateinit var customer_id: EditText
    lateinit var customer_name: EditText
    lateinit var begin: EditText
    lateinit var arise: EditText
    lateinit var end: EditText
    lateinit var check_button: ImageButton
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

    //private var currentSlotIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_debt_report_edit)

        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""

        check_button = findViewById(R.id.check_id_button)
        confirm_button = findViewById(R.id.check_square_button)
        customer_id = findViewById(R.id.customer_id_input)
        customer_name = findViewById(R.id.customer_name)
        begin = findViewById(R.id.beginning_debt_input)
        arise = findViewById(R.id.arising_debt_input)
        end = findViewById(R.id.ending_debt_input)

        // Retrieve the index passed through the intent
        val currentSlotIndex = intent.getIntExtra("index", -1) // Lấy index từ Intent

        // Use the index to set the header text
        if (currentSlotIndex != -1) {
            val headerTextView = findViewById<TextView>(R.id.header_text_view)
            headerTextView.text = "Customer ${currentSlotIndex + 1}"
        }

        // Khôi phục trạng thái nếu có
        if (currentSlotIndex != -1) {
            restoreStateFromPreferences(currentSlotIndex)
        }

        check_button.setOnClickListener {
            val customerId = customer_id.text.toString()
            sendId(customerId)
        }

        confirm_button.setOnClickListener {
            if (currentSlotIndex != -1) {
                saveStateToPreferences(currentSlotIndex) // Lưu trạng thái trước khi trả kết quả về CreateBookImportOrderActivity
            }
            val input_customer_id = customer_id.text.toString()
            val input_customer_name = customer_name.text.toString()
            val input_begin = begin.text.toString()
            val input_arise = arise.text.toString()
            val input_end = end.text.toString()

            if (input_customer_id.isEmpty()
                || input_customer_name.isEmpty()
                || input_begin.isEmpty()
                || input_arise.isEmpty()
                || input_end.isEmpty()
            ) {
                Toast.makeText(this, "Không được bỏ trống bất kỳ thông tin nào", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val input_beginning = begin.text.toString().toIntOrNull() ?: 0
                val input_arising = arise.text.toString().toIntOrNull() ?: 0
                val input_ending = end.text.toString().toIntOrNull() ?: 0

                if (input_ending != input_beginning + input_arising) {
                    Toast.makeText(
                        this,
                        "End debt is not equal to Begin debt + Arise debt. Please correct the values.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //saveStateToPreferences(currentSlotIndex) // Lưu trạng thái khi mọi thứ hợp lệ
                    val resultIntent = Intent()
                    resultIntent.putExtra("customer_id", customer_id.text.toString())
                    resultIntent.putExtra("customer_name", customer_name.text.toString())
                    resultIntent.putExtra("begin_debt", input_begin)
                    resultIntent.putExtra("arise_debt", input_arise)
                    resultIntent.putExtra("end_debt", input_end)
                    resultIntent.putExtra("index", currentSlotIndex)

                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    private fun sendId(id: String) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.CHECK_CUSTOMER_ID_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
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
                    response.body()?.let { customerInfo ->
                        customer_name.setText(customerInfo.customerName)
                    }
                    Toast.makeText(this@MonthlyDebtReportEditActivity, "Customer ID check successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@MonthlyDebtReportEditActivity, "Failed to check customer ID", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CustomerInfo>, t: Throwable) {
                Toast.makeText(this@MonthlyDebtReportEditActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Lưu trạng thái khi nhấn `check_square_button`
    private fun saveStateToPreferences(index: Int) {
        val sharedPreferences = getSharedPreferences("DebtReportEditPrefs_$index", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Lưu thông tin nhập liệu và tiến độ SeekBar
        editor.putString("customerId", customer_id.text.toString())
        editor.putString("customerName", customer_name.text.toString())
        editor.putString("begin", begin.text.toString())
        editor.putString("arise", arise.text.toString())
        editor.putString("end", end.text.toString())

        editor.apply() // Lưu lại
    }

    // Khôi phục trạng thái khi quay lại từ `DebtReport`
    private fun restoreStateFromPreferences(index: Int) {
        val sharedPreferences = getSharedPreferences("DebtReportEditPrefs_$index", MODE_PRIVATE)

        // Khôi phục trạng thái
        customer_id.setText(sharedPreferences.getString("customerId", ""))
        customer_name.setText(sharedPreferences.getString("customerName", ""))
        begin.setText(sharedPreferences.getString("begin", ""))
        arise.setText(sharedPreferences.getString("arise", ""))
        end.setText(sharedPreferences.getString("end", ""))
    }

    fun goToDebtReportActivity(view: View) {
        val intent = Intent(this, MonthlyDebtReportActivity::class.java)
        startActivity(intent)
    }
}
