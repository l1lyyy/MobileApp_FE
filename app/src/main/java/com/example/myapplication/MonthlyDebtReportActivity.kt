package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

class MonthlyDebtReportActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var date: EditText
    lateinit var confirm_button: ImageButton
    private lateinit var postApi: PostApi
    private lateinit var token: String

    // Danh sách các `TextView` và `ImageButton` cho các slot
    private lateinit var customerIds: List<TextView>
    private lateinit var customerNames: List<TextView>
    private lateinit var begins: List<TextView>
    private lateinit var arises: List<TextView>
    private lateinit var ends: List<TextView>
    private lateinit var editButtons: List<ImageButton>
    private var currentSlotIndex: Int = -1  // Theo dõi slot hiện tại

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_debt_report)

        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = preferences.getString("token","") ?: ""

        customerIds = listOf(
            findViewById(R.id.customer_id_1), findViewById(R.id.customer_id_2), findViewById(R.id.customer_id_3),
            findViewById(R.id.customer_id_4), findViewById(R.id.customer_id_5), findViewById(R.id.customer_id_6),
            findViewById(R.id.customer_id_7), findViewById(R.id.customer_id_8), findViewById(R.id.customer_id_9),
            findViewById(R.id.customer_id_10)
        )

        customerNames = listOf(
            findViewById(R.id.customer_name_1), findViewById(R.id.customer_name_2), findViewById(R.id.customer_name_3),
            findViewById(R.id.customer_name_4), findViewById(R.id.customer_name_5), findViewById(R.id.customer_name_6),
            findViewById(R.id.customer_name_7), findViewById(R.id.customer_name_8), findViewById(R.id.customer_name_9),
            findViewById(R.id.customer_name_10)
        )

        begins = listOf(
            findViewById(R.id.begin_1), findViewById(R.id.begin_2), findViewById(R.id.begin_3),
            findViewById(R.id.begin_4), findViewById(R.id.begin_5), findViewById(R.id.begin_6),
            findViewById(R.id.begin_7), findViewById(R.id.begin_8), findViewById(R.id.begin_9),
            findViewById(R.id.begin_10)
        )

        arises = listOf(
            findViewById(R.id.arise_1), findViewById(R.id.arise_2), findViewById(R.id.arise_3),
            findViewById(R.id.arise_4), findViewById(R.id.arise_5), findViewById(R.id.arise_6),
            findViewById(R.id.arise_7), findViewById(R.id.arise_8), findViewById(R.id.arise_9),
            findViewById(R.id.arise_10)
        )

        ends = listOf(
            findViewById(R.id.end_1), findViewById(R.id.end_2), findViewById(R.id.end_3),
            findViewById(R.id.end_4), findViewById(R.id.end_5), findViewById(R.id.end_6),
            findViewById(R.id.end_7), findViewById(R.id.end_8), findViewById(R.id.end_9),
            findViewById(R.id.end_10)
        )

        editButtons = listOf(
            findViewById(R.id.edit_button_1), findViewById(R.id.edit_button_2), findViewById(R.id.edit_button_3),
            findViewById(R.id.edit_button_4), findViewById(R.id.edit_button_5), findViewById(R.id.edit_button_6),
            findViewById(R.id.edit_button_7), findViewById(R.id.edit_button_8), findViewById(R.id.edit_button_9),
            findViewById(R.id.edit_button_10)
        )

        // Add listeners for edit buttons (for each slot)
        for (i in editButtons.indices) {
            editButtons[i].setOnClickListener {
                currentSlotIndex = i  // Cập nhật slot hiện tại
                saveStateToPreferences()
                val intent = Intent(this, MonthlyDebtReportEditActivity::class.java)
                intent.putExtra("index", i)
                resultLauncher.launch(intent)
            }
        }

        // Thiết lập resultLauncher để nhận dữ liệu từ MonthlyDebtReportEditActivity
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && currentSlotIndex != -1) {
                val data = result.data
                val id = data?.getStringExtra("customer_id")
                val name = data?.getStringExtra("customer_name")
                val begin = data?.getStringExtra("begin_debt")
                val arise = data?.getStringExtra("arise_debt")
                val end = data?.getStringExtra("end_debt")

                // Cập nhật các `TextView` trong slot hiện tại
                customerIds[currentSlotIndex].text = "$id\n"
                customerNames[currentSlotIndex].text = "$name\n"
                begins[currentSlotIndex].text = "$begin\n"
                arises[currentSlotIndex].text = "$arise\n"
                ends[currentSlotIndex].text = "$end\n"

                Log.d("MonthlyDebtReport", "Slot $currentSlotIndex - Customer ID: $id, Customer Name: $name, Begin: $begin, Arise: $arise, End: $end")
            }
        }

        // Initialize the EditText for date input
        date = findViewById(R.id.date_input)
        confirm_button =findViewById(R.id.check_square_button)
        confirm_button.setOnClickListener {
            val date_res = formatDateString(date.text.toString())
            val debtReports = mutableListOf<DebtReport>()

            for (i in customerIds.indices) {
                val customerId = customerIds[i].text.toString().trim()
                val customerName = customerNames[i].text.toString().trim()
                val begin = begins[i].text.toString().trim().toIntOrNull() ?: 0
                val arise = arises[i].text.toString().trim().toIntOrNull() ?: 0
                val end = ends[i].text.toString().trim().toIntOrNull() ?: 0

                if (customerId.isNotEmpty() && customerName.isNotEmpty() && begin > 0 && arise > 0 && end > 0) {
                    debtReports.add(DebtReport(date_res,customerId, customerName, begin, arise, end ))
                }
            }
            sendDebtReport(debtReports)
            clearAllPreferences()
            val intent = Intent(this, MonthlyDebtReportActivity::class.java)
            startActivity(intent)
        }

        // Restore the state from SharedPreferences
        restoreStateFromPreferences()

        // Set up Calendar Button
        findViewById<ImageButton>(R.id.calendar_button).setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun sendDebtReport(debtReport: List<DebtReport>)
    {
        if(debtReport.isEmpty())
        {
            Toast.makeText(this, "No valid data to send", Toast.LENGTH_SHORT).show()
            return
        }

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val builder = Retrofit.Builder()
            .baseUrl(PostApi.BASE_REPORT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())

        val retrofit = builder.build()
        postApi = retrofit.create(PostApi::class.java)

        val gson = Gson()
        val json = gson.toJson(debtReport)
        Log.d("importOrders JSON", json)

        val call = postApi.sendDebtReport("Token $token", debtReport)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MonthlyDebtReportActivity, "sales invoice sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    println("Error: $statusCode, $errorBody")
                    Toast.makeText(this@MonthlyDebtReportActivity, "Failed to send sales invoice", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MonthlyDebtReportActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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

    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("DebtReportPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("dateInputText", date.text.toString())

        for (i in customerIds.indices) {
            editor.putString("customerId_$i", customerIds[i].text.toString())
            editor.putString("customerName_$i", customerNames[i].text.toString())
            editor.putString("begin_$i", begins[i].text.toString())
            editor.putString("arise_$i", arises[i].text.toString())
            editor.putString("end_$i", ends[i].text.toString())
        }

        editor.apply()
    }

    private fun restoreStateFromPreferences() {
        val sharedPreferences = getSharedPreferences("DebtReportPrefs", MODE_PRIVATE)
        date.setText(sharedPreferences.getString("dateInputText", ""))

        for (i in customerIds.indices) {
            customerIds[i].text = sharedPreferences.getString("customerId_$i", "ID")
            customerNames[i].text = sharedPreferences.getString("customerName_$i", "Customer " + (i+1).toString())
            begins[i].text = sharedPreferences.getString("begin_$i", "Begin")
            arises[i].text = sharedPreferences.getString("arise_$i", "Arise")
            ends[i].text = sharedPreferences.getString("end_$i", "End")
        }
    }

    private fun clearPreferences() {
        val sharedPreferences = getSharedPreferences("DebtReportPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    override fun onBackPressed() {
        clearAllPreferences() // Xóa tất cả các trạng thái
        super.onBackPressed() // Quay lại dashboard
    }

    private fun clearAllPreferences() {
        // Xóa trạng thái của DebtReportActivity
        val sharedPreferencesOrder = getSharedPreferences("DebtReportPrefs", MODE_PRIVATE)
        sharedPreferencesOrder.edit().clear().apply()

        // Xóa trạng thái của từng DebtReportEditActivity
        for (i in customerIds.indices) {
            val sharedPreferencesEdit = getSharedPreferences("DebtReportEditPrefs_$i", MODE_PRIVATE)
            sharedPreferencesEdit.edit().clear().apply()
        }
    }

    fun goToMonthlyReportActivity(view: View) {
        // Save the current state before switching activities
        clearAllPreferences()
        val intent = Intent(this, MonthlyReportActivity::class.java)
        startActivity(intent)
    }

}