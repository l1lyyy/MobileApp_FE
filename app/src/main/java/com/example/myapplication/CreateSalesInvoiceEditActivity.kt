package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateSalesInvoiceEditActivity : AppCompatActivity() {
    private lateinit var book_name: EditText
    //private lateinit var category: EditText
    private lateinit var amount: EditText
    private lateinit var price: EditText
    private lateinit var confirm_button: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sales_invoice_edit)

        book_name = findViewById(R.id.book_id_input)
        //category = findViewById(R.id.categories_input)
        amount = findViewById(R.id.amount_input)
        price = findViewById(R.id.price_input)
        confirm_button = findViewById(R.id.check_square_button)

        confirm_button.setOnClickListener{
            val input_book_name = book_name.text.toString()
            //val input_category = category.text.toString()
            val input_amount = amount.text.toString()
            val input_price = price.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("bookname",input_book_name)
            //resultIntent.putExtra("category",input_category)
            resultIntent.putExtra("amount",input_amount)
            resultIntent.putExtra("price",input_price)

            setResult(RESULT_OK,resultIntent)
            finish()

        }
    }

    fun goToCreateSalesInvoiceActivity(view: View) {
        val intent = Intent(this, CreateSalesInvoiceActivity::class.java)
        startActivity(intent)
    }

    fun goToVerifyActivity(view: View) {
        val intent = Intent(this, CreateSalesInvoiceActivity::class.java)
        startActivity(intent)
    }
}