package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.Gson
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Address

class CreateReceiptActivity : AppCompatActivity() {
    lateinit var customer_id: EditText
    lateinit var full_name: EditText
    lateinit var address: EditText
    lateinit var phone_number: EditText
    lateinit var email: EditText
    lateinit var payment_date: EditText
    lateinit var paid_amount: EditText
    lateinit var check_button: ImageButton
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

<<<<<<< HEAD
=======
    // Khai báo biến customer_name
    private lateinit var customer_name: String

>>>>>>> dd2ea717c96839412ebb34a2696d549fe18f3c91
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_receipt)

        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""

        customer_id = findViewById(R.id.customer_id_input)
<<<<<<< HEAD
        //val full_name = findViewById<EditText>(R.id.)
        //val address = findViewById<EditText>(R.id.)
        //val phone_number = findViewById<EditText>(R.id.)
        //val email = findViewById<EditText>(R.id.)
=======
        full_name = findViewById(R.id.fullname)
        address = findViewById(R.id.address)
        phone_number = findViewById(R.id.phone_number)
        email = findViewById(R.id.email)
>>>>>>> dd2ea717c96839412ebb34a2696d549fe18f3c91
        payment_date = findViewById(R.id.date_input)
        paid_amount = findViewById(R.id.paid_amount_input)
        confirm_button = findViewById(R.id.check_square_button)
        check_button = findViewById(R.id.check_id_button)

        // Set up Calendar Button
        val calendar_button = findViewById<ImageButton>(R.id.calendar_button)
        calendar_button.setOnClickListener {
            showDatePickerDialog()
        }

<<<<<<< HEAD
        confirm_button.setOnClickListener {
            val customer_id_res = customer_id.text.toString()
            //val full_name_res = full_name.text.toString()
            //val address_res = address.text.toString()
            //val phone_number_res = phone_number.text.toString()
            //val email_res = email.text.toString()
            val payment_date_res = payment_date.text.toString()
            val paid_amount_res = paid_amount.text.toString()
            val paidAmountDouble = paid_amount_res.toDoubleOrNull() ?: 0.0
            sendReceipt(customer_id_res,"Trang Minh Nhut","Vinhomes Tan Cang","255182193","minhnhut13@icloud.com",payment_date_res,paidAmountDouble,token)

            // Send data to the next activity
            val intent = Intent(this, CreateReceiptBillActivity::class.java)
            intent.putExtra("CUSTOMER_ID", customer_id_res)
            intent.putExtra("DATE", payment_date_res)
            intent.putExtra("PAID_AMOUNT", paidAmountDouble)
            startActivity(intent)
=======
        // solve
        check_button.setOnClickListener {
            val customer_id_res = customer_id.text.toString()
            sendId(customer_id_res)
>>>>>>> dd2ea717c96839412ebb34a2696d549fe18f3c91
        }

        confirm_button =findViewById(R.id.check_square_button)
        confirm_button.setOnClickListener {
            val customer_id_res = customer_id.text.toString().trim()
            val full_name_res = full_name.text.toString().trim()
            val address_res = address.text.toString().trim()
            val phone_number_res = phone_number.text.toString().trim()
            val email_res = email.text.toString().trim()
            val payment_date_res = formatDateString(payment_date.text.toString())
            val paid_amount_res = paid_amount.text.toString().trim().toIntOrNull() ?: 0

            if (customer_id_res.isEmpty() || full_name_res.isEmpty() || address_res.isEmpty()
                || phone_number_res.isEmpty() || email_res.isEmpty() || payment_date_res.isEmpty()
                || paid_amount_res == 0) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                sendReceipt(customer_id_res, full_name_res, address_res, phone_number_res, email_res,
                    payment_date_res, paid_amount_res, token)

                // Send data to the next activity
                val intent = Intent(this, CreateReceiptBillActivity::class.java)
                intent.putExtra("PAID_AMOUNT", paid_amount_res)
                intent.putExtra("CUSTOMER_ID", customer_id_res)
                intent.putExtra("CUSTOMER_NAME", customer_name)
                intent.putExtra("DATE", payment_date_res)
                startActivity(intent)
            }

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
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                payment_date.setText(dateFormat.format(selectedDate.time))
            },
            year, month, day
        )
        datePickerDialog.show()
    }

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
                        full_name.setText(CustomerInfo.customerName)
                        address.setText(CustomerInfo.customerAddress)
                        phone_number.setText(CustomerInfo.customerPhone)
                        email.setText(CustomerInfo.customerEmail)

                        // Gán full_name vào customer_name
                        customer_name = CustomerInfo.customerName
                    }
                    Toast.makeText(
                        this@CreateReceiptActivity,
                        "customer ID check successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(
                        this@CreateReceiptActivity,
                        "Failed to check customer ID",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CustomerInfo>, t: Throwable) {
                Toast.makeText(
                    this@CreateReceiptActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sendReceipt(id: String, fullname: String, address: String,phonenumber:String, email: String, paymentdate: String, paidamount: Int, token: String)
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.RECEIPT_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)
        val paidAmountInt = paidamount.toInt()
        val receipt = CustomerData(id,fullname,address,phonenumber,email,paymentdate,paidAmountInt)
        val gson = Gson()
        val json = gson.toJson(receipt)
        Log.d("receipt JSON", json)
        val call = postApi.sendReceipt("Token $token", receipt)

        call.enqueue(object: Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                if (response.isSuccessful)
                {
                    Toast.makeText(this@CreateReceiptActivity, "receipt sent successfully", Toast.LENGTH_SHORT).show()
                } else
                {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateReceiptActivity, "Failed to send receipt", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
            {
                Toast.makeText(this@CreateReceiptActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> dd2ea717c96839412ebb34a2696d549fe18f3c91
