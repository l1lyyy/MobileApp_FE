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

class CreateSalesInvoiceEditActivity : AppCompatActivity() {
    private lateinit var bookName: EditText
    private lateinit var amount: EditText
    private lateinit var price: EditText
    private lateinit var confirmButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sales_invoice_edit)

        bookName = findViewById(R.id.book_id_input)
        amount = findViewById(R.id.amount_input)
        price = findViewById(R.id.price_input)
        confirmButton = findViewById(R.id.check_square_button)
        seekBar = findViewById(R.id.seekBar)
        seekBarValue = findViewById(R.id.seekBarValue)

        // Retrieve the edit type passed through the intent
        val editType = intent.getStringExtra("book_type")

        // Use the edit type to customize the activity, for example, changing the header text
        val headerTextView = findViewById<TextView>(R.id.header_text_view)
        headerTextView.text = "Edit $editType"

        confirmButton.setOnClickListener {
            val inputBookName = bookName.text.toString()
            val inputAmount = amount.text.toString()
            val inputPrice = price.text.toString()
            val resultIntent = Intent().apply {
                putExtra("bookname", inputBookName)
                putExtra("amount", inputAmount)
                putExtra("price", inputPrice)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Apply the setupSeekBarWithEditText method
        setupSeekBarWithEditText(seekBar, amount, seekBarValue)
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

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

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
