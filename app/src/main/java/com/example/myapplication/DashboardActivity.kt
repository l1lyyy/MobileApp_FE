package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
    }

    fun goToAddNewBookActivity(view: View) {
        val intent = Intent(this, AddNewBookActivity::class.java)
        startActivity(intent)
    }

    fun goToBooksImportActivity(view: View) {
        val intent = Intent(this, CreateBookImportOrderActivity::class.java)
        startActivity(intent)
    }

    fun goToSearchBooksActivity(view: View) {
        val intent = Intent(this, SearchBookActivity::class.java)
        startActivity(intent)
    }

    fun goToCreateSalesInvoiceActivity(view: View) {
        val intent = Intent(this, CreateSalesInvoiceActivity::class.java)
        startActivity(intent)
    }

    fun goToCreateReceiptActivity(view: View) {
        val intent = Intent(this, CreateReceiptActivity::class.java)
        startActivity(intent)
    }

    fun goToMonthlyReportActivity(view: View) {
        val intent = Intent(this, MonthlyReportActivity::class.java)
        startActivity(intent)
    }

    fun goToSettingActivity(view: View) {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    fun goToUserActivity(view: View) {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
}