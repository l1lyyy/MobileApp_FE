package com.example.myapplication

import android.content.Intent
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateSalesInvoiceEditActivity : AppCompatActivity() {
    private lateinit var book_name: EditText
    private lateinit var amount: EditText
    private lateinit var price: EditText
    private lateinit var confirm_button: ImageButton

    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sales_invoice_edit)

        book_name = findViewById(R.id.book_id_input)
        //category = findViewById(R.id.categories_input)
        amount = findViewById(R.id.amount_input)
        price = findViewById(R.id.price_input)
        confirm_button = findViewById(R.id.check_square_button)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)

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

        // Update EditText when SeekBar is changed
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                amount.setText(progress.toString())
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
        amount.addTextChangedListener(object : TextWatcher {
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