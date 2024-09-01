package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class BookData(
    @SerializedName("book_id")
    var bookId: String,

    @SerializedName("book_name")
    var bookName: String,

    @SerializedName("author")
    var author: String,

    @SerializedName("category")
    var category: String,

    @SerializedName("price")
    var price: Int,

    @SerializedName("amount")
    var amount: Int
)
