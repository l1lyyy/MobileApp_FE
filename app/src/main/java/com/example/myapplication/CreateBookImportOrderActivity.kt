package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import android.content.SharedPreferences
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
<<<<<<< HEAD
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var date: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String
=======

    private lateinit var dateInput: EditText

>>>>>>> Nhu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_book_import_order)
<<<<<<< HEAD
        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""
        val result_id = findViewById<TextView>(R.id.book_ID_1)
        val result_name = findViewById<TextView>(R.id.book_name_1)
        val result_author = findViewById<TextView>(R.id.author_1)
        val result_amount = findViewById<TextView>(R.id.amount_1)
=======

        // Initialize the EditText for date input
        dateInput = findViewById(R.id.date_input)

        // Restore the state from SharedPreferences
        restoreStateFromPreferences()

        // Set up the navigation to the edit activities
        findViewById<ImageButton>(R.id.edit_button_1).setOnClickListener {
            goToEditActivity("book 1")
        }

        findViewById<ImageButton>(R.id.edit_button_2).setOnClickListener {
            goToEditActivity("book 2")
        }

        findViewById<ImageButton>(R.id.edit_button_3).setOnClickListener {
            goToEditActivity("book 3")
        }

        findViewById<ImageButton>(R.id.edit_button_4).setOnClickListener {
            goToEditActivity("book 4")
        }

        findViewById<ImageButton>(R.id.edit_button_5).setOnClickListener {
            goToEditActivity("book 5")
        }

        findViewById<ImageButton>(R.id.edit_button_6).setOnClickListener {
            goToEditActivity("book 6")
        }

        findViewById<ImageButton>(R.id.edit_button_7).setOnClickListener {
            goToEditActivity("book 7")
        }

        findViewById<ImageButton>(R.id.edit_button_8).setOnClickListener {
            goToEditActivity("book 8")
        }

        findViewById<ImageButton>(R.id.edit_button_9).setOnClickListener {
            goToEditActivity("book 9")
        }

        findViewById<ImageButton>(R.id.edit_button_10).setOnClickListener {
            goToEditActivity("book 10")
        }

        // Set up the calendar button click listener
        findViewById<View>(R.id.calendar_button).setOnClickListener {
            showDatePickerDialog()
        }
>>>>>>> Nhu
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
<<<<<<< HEAD
=======


    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save the current state
        editor.putString("dateInputText", dateInput.text.toString())
        editor.apply()
    }

    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)

        // Restore the state
        dateInput.setText(sharedPreferences.getString("dateInputText", ""))
    }

    private fun clearPreferences() {
        val sharedPreferences = getSharedPreferences("CreateBookImportOrderPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

>>>>>>> Nhu
    fun goToDashboardActivity(view: View) {
        // Save the current state before switching activities
        clearPreferences()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun goToEditActivity(editType: String) {
        // Navigate to EditActivity with the type of edit
        saveStateToPreferences()
        val intent = Intent(this, CreateBookImportOderEditActivity::class.java)
        intent.putExtra("book_type", editType)
        startActivity(intent)
    }

<<<<<<< HEAD
}
=======

}
>>>>>>> Nhu
