package com.example.myapplication
import com.google.gson.annotations.SerializedName
data class DebtReport(
    @SerializedName("month")
    var date: String,

    @SerializedName("customer_id")
    var customer_id: String,

    @SerializedName("customer_name")
    var customer_name: String,

    @SerializedName("begin")
    var begin: Int,

    @SerializedName("arise")
    var arise: Int,

    @SerializedName("end")
    var end: Int
)

data class InventoryReport(
    @SerializedName("month")
    var date: String,
    @SerializedName("book_id")
    var book_id: String,
    @SerializedName("book_name")
    var book_name: String,
    @SerializedName("begin")
    var begin: Int,
    @SerializedName("arise")
    var arise: Int,
    @SerializedName("end")
    var end: Int,
)
