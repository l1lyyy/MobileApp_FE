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
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateSalesInvoiceActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var customer_name: EditText
    lateinit var date: EditText
    lateinit var create: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sales_invoice)
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""
        val result_book_name = findViewById<TextView>(R.id.book_name_1)
        //val result_category = findViewById<TextView>(R.id.categories_1)
        val result_amount = findViewById<TextView>(R.id.amount_1)
        val result_price = findViewById<TextView>(R.id.price_1)
        val button = findViewById<ImageButton>(R.id.edit_button_1)

        var amount_res = 0
        var price_res = 0.0f
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()

        ) {result ->
            if (result.resultCode== RESULT_OK) {
                val data = result.data
                val bookname= data?.getStringExtra("bookname")
                //val category = data?.getStringExtra("category")
                val amount = data?.getStringExtra("amount")
                val priceString = data?.getStringExtra("price")
                val price = priceString?.toFloatOrNull() ?: 0.0f
                result_book_name.text = "$bookname\n"
                //result_category.text = "$category\n"
                result_amount.text = "$amount\n"
                result_price.text = "$price\n"

                // Get the displayed text (remove the newline character if necessary)
                val displayedAmount = result_amount.text.toString().trim()
                // Convert the strings to integers
                amount_res= displayedAmount.toIntOrNull() ?: 0
                price_res = price
            }
        }
        button.setOnClickListener{
            val intent = Intent(this, CreateSalesInvoiceEditActivity::class.java)
            resultLauncher.launch(intent)
        }

        customer_name = findViewById(R.id.customer_name_input)
        date = findViewById(R.id.date_input)
        create = findViewById(R.id.check_square_button)
        create.setOnClickListener{
            val customer_name_res = customer_name.text.toString()
            val date_res = date.text.toString()

            sendSalesInvoice("Nhut",date_res,result_book_name.text.toString(),"null", amount_res, price_res,token)
        }
    }
    private fun sendSalesInvoice(customername: String, date: String, book_name: String, category: String, amount: Int, price: Float,token: String )
    {
        val builder = Retrofit.Builder()
            .baseUrl(PostApi.INVOICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)

        val invoice = Salesinvoice("",customername,date,book_name,category, amount,price)
        val call = postApi.sendSalesInvoice("Token $token", invoice)

        call.enqueue(object: Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                if (response.isSuccessful)
                {
                    Toast.makeText(this@CreateSalesInvoiceActivity, "Invoice sent successfully", Toast.LENGTH_SHORT).show()
                } else
                {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateSalesInvoiceActivity, "Failed to send invoice", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
            {
                Toast.makeText(this@CreateSalesInvoiceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun goToEditActivity(view: View) {
        val intent = Intent(this, CreateSalesInvoiceEditActivity::class.java)
        startActivity(intent)
    }

}