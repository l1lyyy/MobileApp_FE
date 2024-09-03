package com.example.myapplication
import com.google.gson.annotations.SerializedName

data class ImportOrder(
    @SerializedName("book_id")
    var id: String,

    @SerializedName("book_name")
    var book: String,

    @SerializedName("author")
    var author: String,

    @SerializedName("amount")
    var amount: Int,

    @SerializedName("date")
    var date: String
)

data class ImportOrderRequest(
    @SerializedName("date")
    var date: String,

    @SerializedName("details")
    var details: List<ImportOrder>
)