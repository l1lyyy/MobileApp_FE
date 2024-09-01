package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Address
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

class CreateReceiptActivity : AppCompatActivity() {
    lateinit var customer_id: EditText
    lateinit var full_name: EditText
    lateinit var address: EditText
    lateinit var phone_number: EditText
    lateinit var email: EditText
    lateinit var payment_date: EditText
    lateinit var paid_amount: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_receipt)
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""
        val customer_id = findViewById<EditText>(R.id.customer_id_input)
        //val full_name = findViewById<EditText>(R.id.)
        //val address = findViewById<EditText>(R.id.)
        //val phone_number = findViewById<EditText>(R.id.)
        //val email = findViewById<EditText>(R.id.)
        val payment_date = findViewById<EditText>(R.id.date_input)
        val paid_amount = findViewById<EditText>(R.id.paid_amount_input)
        confirm_button = findViewById(R.id.check_square_button)
        confirm_button.setOnClickListener{
            val customer_id_res = customer_id.text.toString()
            //val full_name_res = full_name.text.toString()
            //val address_res = address.text.toString()
            //val phone_number_res = phone_number.text.toString()
            //val email_res = email.text.toString()
            val payment_date_res = payment_date.text.toString()
            val paid_amount_res = paid_amount.text.toString()
            val paidAmountDouble = paid_amount_res.toDoubleOrNull() ?: 0.0
            sendReceipt(customer_id_res,"Trang Minh Nhut","Vinhomes Tan Cang","255182193","minhnhut13@icloud.com",payment_date_res,paidAmountDouble,token)
        }
    }

    private fun sendReceipt(id: String, fullname: String, address: String,phonenumber:String, email: String, paymentdate: String, paidamount: Double, token: String)
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

}
