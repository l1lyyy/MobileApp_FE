package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateSalesInvoiceEditActivity : AppCompatActivity() {
    lateinit var book_id: EditText
    lateinit var book_name: EditText
    lateinit var amount: EditText
    lateinit var price: EditText
    lateinit var check_button: ImageButton
    lateinit var confirm_button: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView
    private lateinit var postApi: PostApi
    private lateinit var token: String

    private var currentSlotIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sales_invoice_edit)

        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""

        check_button = findViewById(R.id.check_id_button)
        book_id = findViewById(R.id.book_id_input)
        book_name = findViewById(R.id.book_name)
        amount = findViewById(R.id.amount_input)
        price = findViewById(R.id.price_input)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)
        confirm_button = findViewById(R.id.check_square_button)

        currentSlotIndex = intent.getIntExtra("index", -1)

        val editType = intent.getStringExtra("book_type")
        val headerTextView = findViewById<TextView>(R.id.header_text_view)
        headerTextView.text = "Edit $editType"

        if (currentSlotIndex != -1) {
            restoreStateFromPreferences(currentSlotIndex)
        }

        check_button.setOnClickListener {
            val book_id_res = book_id.text.toString()
            sendId(book_id_res)
        }

        confirm_button.setOnClickListener {
            if (currentSlotIndex != -1) {
                saveStateToPreferences(currentSlotIndex)
            }

            val input_book_id = book_id.text.toString()
            val input_book_name = book_name.text.toString()
            val input_amount = amount.text.toString()
            val input_price = price.text.toString()

            if (input_book_id.isEmpty() || input_book_name.isEmpty() || input_amount.isEmpty() || input_price.isEmpty()) {
                Toast.makeText(this, "Không được bỏ trống bất kỳ thông tin nào", Toast.LENGTH_SHORT).show()
            } else {
                val resultIntent = Intent()
                resultIntent.putExtra("bookid", input_book_id)
                resultIntent.putExtra("bookname", input_book_name)
                resultIntent.putExtra("amount", input_amount)
                resultIntent.putExtra("price", input_price)
                resultIntent.putExtra("index", currentSlotIndex)

                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

        setupSeekBarWithEditText(seekBar, amount, seekBarValue)
    }

    private fun setupSeekBarWithEditText(seekBar: SeekBar, editText: EditText, seekBarValue: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (editText.text.toString().toIntOrNull() != progress) {
                    editText.setText(progress.toString())
                }
                seekBarValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()) {
                    try {
                        val value = s.toString().toInt()
                        if (seekBar.progress != value) {
                            seekBar.progress = value
                        }
                    } catch (e: NumberFormatException) {}
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun sendId(id: String)
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder().baseUrl(PostApi.CHECK_BOOK_ID_URL).addConverterFactory(
            GsonConverterFactory.create()).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)

        val id_search = BookId(id)
        val gson = Gson()
        val json = gson.toJson(id_search)
        Log.d("book id JSON", json)

        val call = postApi.sendBookId("Token $token", id_search)

        call.enqueue(object: Callback<BookInfo> {
            override fun onResponse(call: Call<BookInfo>, response: Response<BookInfo>) {
                if (response.isSuccessful) {
                    response.body()?.let { BookInfo ->
                        // Gán giá trị vào EditText
                        book_name.setText(BookInfo.bookName)
                    }
                    Toast.makeText(this@CreateSalesInvoiceEditActivity, "book ID check successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateSalesInvoiceEditActivity, "Failed to check book ID", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookInfo>, t: Throwable) {
                Toast.makeText(this@CreateSalesInvoiceEditActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveStateToPreferences(index: Int) {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoiceEditPrefs_$index", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("bookId", book_id.text.toString())
        editor.putString("bookName", book_name.text.toString())
        editor.putString("amount", amount.text.toString())
        editor.putString("price", price.text.toString())
        editor.putInt("seekBarProgress", seekBar.progress)

        editor.apply()
    }

    private fun restoreStateFromPreferences(index: Int) {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoiceEditPrefs_$index", MODE_PRIVATE)

        book_id.setText(sharedPreferences.getString("bookId", ""))
        book_name.setText(sharedPreferences.getString("bookName", ""))
        amount.setText(sharedPreferences.getString("amount", ""))
        price.setText(sharedPreferences.getString("price", ""))
        seekBar.progress = sharedPreferences.getInt("seekBarProgress", 0)
        seekBarValue.text = seekBar.progress.toString()
    }

    private fun clearPreferences(index: Int) {
        val sharedPreferences = getSharedPreferences("CreateSalesInvoiceEditPrefs_$index", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    override fun onBackPressed() {
        if (currentSlotIndex != -1) {
            clearPreferences(currentSlotIndex)
        }
        super.onBackPressed()
    }

    fun goToCreateSalesInvoiceActivity(view: View) {
        val intent = Intent(this, CreateSalesInvoiceActivity::class.java)
        startActivity(intent)
    }
}