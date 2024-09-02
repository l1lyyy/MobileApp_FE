package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class CreateReceiptBillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_receipt_bill)

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
    }

    fun goToCreateReceiptActivity(view: View) {
        val intent = Intent(this, CreateReceiptActivity::class.java)
        startActivity(intent)
    }
}
