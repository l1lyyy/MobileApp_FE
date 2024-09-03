package com.example.myapplication
import com.google.gson.annotations.SerializedName
data class Salesinvoice (
    @SerializedName("customer_id")
    val id: String,

    @SerializedName("customer_name")
    val customer_name: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("book_id")
    val book_id: String,

    @SerializedName("book_name")
    val book_name: String,


    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Int
)

data class CustomerId(
    @SerializedName("customer_id")
    val id: String
)

data class CustomerInfo(
    @SerializedName("customer_name")
    val customerName: String,
    @SerializedName("address")
    val customerAddress: String,
    @SerializedName("phone_number")
    val customerPhone: String,
    @SerializedName("email")
    val customerEmail: String
)