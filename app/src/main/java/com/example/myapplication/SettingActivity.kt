package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var minimumImportInput: EditText
    private lateinit var numImportStockLessThanInput: EditText
    private lateinit var maximumDebtInput: EditText
    private lateinit var arisingInventory: EditText
    private lateinit var seekBar1: SeekBar
    private lateinit var seekBarValue1: TextView
    private lateinit var seekBar2: SeekBar
    private lateinit var seekBarValue2: TextView
    private lateinit var seekBar3: SeekBar
    private lateinit var seekBarValue3: TextView
    private lateinit var seekBar4: SeekBar
    private lateinit var seekBarValue4: TextView
    private lateinit var applyFilterCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        // Initialize views
        minimumImportInput = findViewById(R.id.minimum_import_input)
        numImportStockLessThanInput = findViewById(R.id.num_import_stock_less_than_input)
        maximumDebtInput = findViewById(R.id.maximum_debt_input)
        arisingInventory = findViewById(R.id.arising_inventory_input)
        seekBar1 = findViewById(R.id.seekBar_1)
        seekBarValue1 = findViewById(R.id.seekBarValue_1)
        seekBar2 = findViewById(R.id.seekBar_2)
        seekBarValue2 = findViewById(R.id.seekBarValue_2)
        seekBar3 = findViewById(R.id.seekBar_3)
        seekBarValue3 = findViewById(R.id.seekBarValue_3)
        seekBar4 = findViewById(R.id.seekBar)
        seekBarValue4 = findViewById(R.id.seekBarValue)
        applyFilterCheckbox = findViewById(R.id.apply_filter_checkbox)

        // Synchronize SeekBar and EditText for minimumImportInput
        setupSeekBarWithEditText(seekBar1, minimumImportInput, seekBarValue1)

        // Synchronize SeekBar and EditText for numImportStockLessThanInput
        setupSeekBarWithEditText(seekBar2, numImportStockLessThanInput, seekBarValue2)

        // Synchronize SeekBar and EditText for maximumDebtInput
        setupSeekBarWithEditText(seekBar3, maximumDebtInput, seekBarValue3)

        // Synchronize SeekBar and EditText for arisingInventory
        setupSeekBarWithEditText(seekBar4, arisingInventory, seekBarValue4)

        // Handle checkbox change
        applyFilterCheckbox.setOnCheckedChangeListener { _, isChecked ->
            // Perform actions based on the checkbox state
            // For example, enable or disable a specific feature
        }
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


    fun goToDashboardActivity(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}
