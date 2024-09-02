package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
<<<<<<< HEAD
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
import android.widget.Toast
import org.json.JSONObject
=======
>>>>>>> Nhu

class CreateBookImportOderEditActivity : AppCompatActivity() {
    lateinit var book_id: EditText
    lateinit var check_button: ImageButton
    lateinit var book_name: EditText
    lateinit var author: EditText
    private lateinit var amountInput: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView
    private lateinit var postApi: PostApi
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_oder_edit)
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""

        book_id = findViewById(R.id.book_id_input)
        check_button = findViewById(R.id.check_id_button)
        book_name = findViewById(R.id.book_name_1)
        author = findViewById(R.id.author_name)

<<<<<<< HEAD
        check_button.setOnClickListener {
            val book_id_res = book_id.text.toString()
            sendId(book_id_res)
        }
    }
=======
        // Retrieve the edit type passed through the intent
        val editType = intent.getStringExtra("book_type")

        // Use the edit type to customize the activity, for example, changing the header text
        val headerTextView = findViewById<TextView>(R.id.header_text_view)
        headerTextView.text = "Edit $editType"

        // Apply the setupSeekBarWithEditText method
        setupSeekBarWithEditText(seekBar, amountInput, seekBarValue)
    }

    private fun setupSeekBarWithEditText(seekBar: SeekBar, editText: EditText, seekBarValue: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Ensure that we are not creating a loop of updates
                if (editText.text.toString().toIntOrNull() != progress) {
                    editText.setText(progress.toString())
                }
                seekBarValue.text = progress.toString()
            }
>>>>>>> Nhu

    private fun sendId(id: String)
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

<<<<<<< HEAD
        val builder = Retrofit.Builder().baseUrl(PostApi.CHECK_BOOK_ID_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
        val retrofit = builder.build()

        postApi = retrofit.create(PostApi::class.java)
        //val idInt = id.toInt()
        val id_search = BookId(id)
        val gson = Gson()
        val json = gson.toJson(id_search)
        Log.d("book id JSON", json)

        val call = postApi.sendBookId("Token $token", id_search)

        call.enqueue(object: Callback<BookInfo> {
            override fun onResponse(call: Call<BookInfo>, response: Response<BookInfo>) {
                if (response.isSuccessful) {
                    response.body()?.let { bookInfo ->
                        // Gán giá trị vào EditText
                        book_name.setText(bookInfo.bookName)
                        author.setText(bookInfo.author)
                    }
                    Toast.makeText(this@CreateBookImportOderEditActivity, "Book ID check successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@CreateBookImportOderEditActivity, "Failed to check book ID", Toast.LENGTH_SHORT).show()
=======
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Avoid updating SeekBar if the value is invalid or if it is being updated by SeekBar
                if (s != null && s.isNotEmpty()) {
                    try {
                        val value = s.toString().toInt()
                        if (seekBar.progress != value) {
                            seekBar.progress = value
                        }
                    } catch (e: NumberFormatException) {
                        // Handle the case where the input is not a valid integer
                    }
>>>>>>> Nhu
                }
            }

            override fun onFailure(call: Call<BookInfo>, t: Throwable) {
                Toast.makeText(this@CreateBookImportOderEditActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun goToBooksImportActivity(view: View) {
        val intent = Intent(this, CreateBookImportOrderActivity::class.java)
        startActivity(intent)
    }
<<<<<<< HEAD

}
=======
}
>>>>>>> Nhu
