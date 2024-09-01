package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.eap.EapSessionConfig.EapAkaPrimeConfig
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.google.gson.Gson
import android.util.Log
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.time.temporal.TemporalAmount

class AddNewBookActivity : AppCompatActivity() {

    private lateinit var priceInput: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView
    lateinit var book_name: EditText
    lateinit var author: EditText
    lateinit var category: EditText
    lateinit var price: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_book)
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""
        book_name = findViewById(R.id.book_input)
        author = findViewById(R.id.author_input)
        category = findViewById(R.id.categories_input)
        price = findViewById(R.id.price_input)
        confirm_button = findViewById(R.id.check_square_button)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)

         //Update EditText when SeekBar is changed
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceInput.setText(progress.toString())
                seekBarValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

        // Update SeekBar when EditText is changed
        priceInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()) {
                    val value = s.toString().toInt()
                    seekBar.progress = value
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })

        confirm_button.setOnClickListener{
            val book_name_res = book_name.text.toString()
            val author_res = author.text.toString()
            val category_res = category.text.toString()
            val price_res = price.text.toString()
            val price_double = price_res.toDoubleOrNull() ?: 0.0
            sendBookData("0", book_name_res,author_res,category_res,price_double,2 )
        }
    }

    private  fun sendBookData(id: String, name: String, author: String, category: String, price: Double,amount: Int)
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.ADD_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)
        val priceInt = price.toInt()
        val bookdata = BookData(id,name,author,category,priceInt,amount)
        val gson = Gson()
        val json = gson.toJson(bookdata)
        Log.d("bookdata JSON", json)
        val call = postApi.sendBookData("Token $token", bookdata)

        call.enqueue(object: Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                if (response.isSuccessful)
                {
                    Toast.makeText(this@AddNewBookActivity, "Add new book successfully", Toast.LENGTH_SHORT).show()
                } else
                {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@AddNewBookActivity, "Failed to Add new book", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
            {
                Toast.makeText(this@AddNewBookActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

}
