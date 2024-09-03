package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var minimumImportInput: EditText
    private lateinit var numImportStockLessThanInput: EditText
    private lateinit var maximumDebtInput: EditText
    private lateinit var minimumInStockInput: EditText
    private lateinit var seekBar1: SeekBar
    private lateinit var seekBarValue1: TextView
    private lateinit var seekBar2: SeekBar
    private lateinit var seekBarValue2: TextView
    private lateinit var seekBar3: SeekBar
    private lateinit var seekBarValue3: TextView
    private lateinit var seekBar4: SeekBar
    private lateinit var seekBarValue4: TextView
    private lateinit var applyFilterCheckbox: CheckBox

    private val defaultMinimumImport = 150
    private val defaultNumImportStockLessThan = 300
    private val defaultMaximumDebt = 20000
    private val defaultMinimumInStock = 20
    private val defaultApplyFilter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        // Initialize views
        minimumImportInput = findViewById(R.id.minimum_import_input)
        numImportStockLessThanInput = findViewById(R.id.num_import_stock_less_than_input)
        maximumDebtInput = findViewById(R.id.maximum_debt_input)
        minimumInStockInput = findViewById(R.id.minimum_in_stock)
        seekBar1 = findViewById(R.id.seekBar_1)
        seekBarValue1 = findViewById(R.id.seekBarValue_1)
        seekBar2 = findViewById(R.id.seekBar_2)
        seekBarValue2 = findViewById(R.id.seekBarValue_2)
        seekBar3 = findViewById(R.id.seekBar_3)
        seekBarValue3 = findViewById(R.id.seekBarValue_3)
        seekBar4 = findViewById(R.id.seekBar)
        seekBarValue4 = findViewById(R.id.seekBarValue)
        applyFilterCheckbox = findViewById(R.id.apply_filter_checkbox)

        // Set input filters for EditTexts other than maximumDebtInput
        setInputFilter(minimumImportInput, 1000)
        setInputFilter(numImportStockLessThanInput, 1000)
        setInputFilter(minimumInStockInput, 1000)
        setInputFilter(maximumDebtInput, 200000)

        // Restore state or set defaults
        restoreStateOrSetDefaults()

        // Synchronize SeekBars with EditTexts
        setupSeekBarWithEditText(seekBar1, minimumImportInput, seekBarValue1)
        setupSeekBarWithEditText(seekBar2, numImportStockLessThanInput, seekBarValue2)
        setupSeekBarWithEditText(seekBar3, maximumDebtInput, seekBarValue3)
        setupSeekBarWithEditText(seekBar4, minimumInStockInput, seekBarValue4)

        // Handle checkbox change
        applyFilterCheckbox.setOnCheckedChangeListener { _, _ -> }

        // Save settings on check_square_button click
        findViewById<View>(R.id.check_square_button).setOnClickListener {
            saveSettings()
        }

        // Handle back button click
        findViewById<View>(R.id.back_button).setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setInputFilter(editText: EditText, maxValue: Int) {
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            try {
                val input = (dest.toString() + source.toString()).toInt()
                if (input in 0..maxValue) null else ""
            } catch (nfe: NumberFormatException) {
                ""
            }
        }
        editText.filters = arrayOf(filter)
    }

    private fun restoreStateOrSetDefaults() {
        val sharedPreferences = getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)

        // Get stored values or set defaults
        minimumImportInput.setText(sharedPreferences.getInt("minimumImport", defaultMinimumImport).toString())
        syncSeekBarWithValue(seekBar1, seekBarValue1, minimumImportInput)

        numImportStockLessThanInput.setText(sharedPreferences.getInt("numImportStockLessThan", defaultNumImportStockLessThan).toString())
        syncSeekBarWithValue(seekBar2, seekBarValue2, numImportStockLessThanInput)

        maximumDebtInput.setText(sharedPreferences.getInt("maximumDebt", defaultMaximumDebt).toString())
        syncSeekBarWithValue(seekBar3, seekBarValue3, maximumDebtInput)

        minimumInStockInput.setText(sharedPreferences.getInt("minimumInStock", defaultMinimumInStock).toString())
        syncSeekBarWithValue(seekBar4, seekBarValue4, minimumInStockInput)

        applyFilterCheckbox.isChecked = sharedPreferences.getBoolean("applyFilter", defaultApplyFilter)
    }

    private fun syncSeekBarWithValue(seekBar: SeekBar, seekBarValue: TextView, editText: EditText) {
        val value = editText.text.toString().toIntOrNull() ?: 0
        seekBar.progress = value
        seekBarValue.text = value.toString()
    }

    private fun setupSeekBarWithEditText(seekBar: SeekBar, editText: EditText, seekBarValue: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
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
                if (s != null && s.isNotEmpty()) {
                    try {
                        val value = s.toString().toInt()
                        seekBar.progress = value
                        seekBarValue.text = value.toString()
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

    private fun saveSettings() {
        saveStateToPreferences()
        Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show()
    }

    private fun saveStateToPreferences() {
        val sharedPreferences = getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("minimumImport", minimumImportInput.text.toString().toInt())
        editor.putInt("numImportStockLessThan", numImportStockLessThanInput.text.toString().toInt())
        editor.putInt("maximumDebt", maximumDebtInput.text.toString().toInt())
        editor.putInt("minimumInStock", minimumInStockInput.text.toString().toInt())
        editor.putBoolean("applyFilter", applyFilterCheckbox.isChecked)

        editor.apply()
    }
}
