package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class Salesinvoice (
    val id: String,
    val customer_name: String,
    val date: String,
    val book_id: String,
    val book_name: String,
    val quantity: Int,
    val price: Int
)

data class CustomerId(
    @SerializedName("customer_id")
    val id: String
)

data class CustomerInfo(
    @SerializedName("customer_name")
    val customerName: String,
    val customerAddress: String,
    val customerPhone: String,
    val customerEmail: String
)