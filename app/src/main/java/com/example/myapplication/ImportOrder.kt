package com.example.myapplication
import com.google.gson.annotations.SerializedName

data class ImportOrder(
    @SerializedName("")
    var id: String,

    @SerializedName("")
    var book: String,

    @SerializedName("")
    var author: String,

    @SerializedName("")
    var amount: Int,

    @SerializedName("")
    var date: String
)

