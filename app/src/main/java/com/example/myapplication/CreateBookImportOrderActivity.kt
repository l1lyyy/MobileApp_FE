package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBookImportOrderActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var date: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_order)
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""
        val result_id = findViewById<TextView>(R.id.book_ID_1)
        val result_name = findViewById<TextView>(R.id.book_name_1)
        val result_author = findViewById<TextView>(R.id.author_1)
        val result_amount = findViewById<TextView>(R.id.amount_1)
    }

    private fun sendImportOrder(id: String, book: String, author: String, amount: String,date: String)
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.IMPORT_URL).addConverterFactory(
            GsonConverterFactory.create()).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)
        val amountInt = amount.toInt()
        //val idInt = id.toInt()
        val import = ImportOrder(id,book,author,amountInt,date)
        val gson = Gson()
        val json = gson.toJson(import)
        Log.d("import JSON",json)
        val call = postApi.sendImportOrder("Token $token", import)

        call.enqueue(object: Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                if (response.isSuccessful)
                {
                    Toast.makeText(this@CreateBookImportOrderActivity, "import order sent successfully", Toast.LENGTH_SHORT).show()
                } else
                {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateBookImportOrderActivity, "Failed to send import order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
            {
                Toast.makeText(this@CreateBookImportOrderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

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