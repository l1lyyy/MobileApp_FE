package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.EditText
<<<<<<< HEAD
=======
import android.widget.ImageButton
>>>>>>> dd2ea717c96839412ebb34a2696d549fe18f3c91
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class CreateReceiptBillActivity : AppCompatActivity() {
    lateinit var paid_amount: EditText
    lateinit var customerId: EditText
    lateinit var customerName: EditText
    lateinit var payment_date: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_receipt_bill)

<<<<<<< HEAD
        val customerName = intent.getStringExtra("CUSTOMER_NAME") ?: ""
        val date = intent.getStringExtra("DATE") ?: ""
        val paidAmountDouble = intent.getDoubleExtra("PAID_AMOUNT", 0.0)
        val paidAmount = paidAmountDouble.toInt()

        // Format the paid amount as currency
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val formattedAmount = numberFormat.format(paidAmount)

        val customerNameEditText = findViewById<EditText>(R.id.customer_name)
        val dateEditText = findViewById<EditText>(R.id.date)
        val paidAmountTextView = findViewById<TextView>(R.id.total_amount)

        customerNameEditText.setText(customerName)
        dateEditText.setText(date)
        paidAmountTextView.text = formattedAmount
=======
        payment_date = findViewById(R.id.date)
        paid_amount = findViewById(R.id.total_amount)
        customerId = findViewById(R.id.customer_id)
        customerName = findViewById(R.id.customer_name)

        val customer_id_res = intent.getStringExtra("CUSTOMER_ID") ?: ""
        val customer_name_res = intent.getStringExtra("CUSTOMER_NAME") ?: ""
        val date_res = intent.getStringExtra("DATE") ?: ""
        val paid_amount_res = intent.getIntExtra("PAID_AMOUNT", 0)

        // Format the paid amount as currency
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val formattedAmount = numberFormat.format(paid_amount_res)

        payment_date.setText(date_res)
        paid_amount.setText(formattedAmount)
        customerId.setText(customer_id_res)
        customerName.setText(customer_name_res)

>>>>>>> dd2ea717c96839412ebb34a2696d549fe18f3c91
    }

    fun goToCreateReceiptActivity(view: View) {
        val intent = Intent(this, CreateReceiptActivity::class.java)
        startActivity(intent)
    }
}
