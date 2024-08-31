package com.example.myapplication

data class CustomerData(
    var id: String,
    var name: String,
    var address: String,
    var phone: String,
    var email: String,
    var payment_date: String,
    var paid_amount: Double
)