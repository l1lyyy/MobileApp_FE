package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class CustomerData(
    @SerializedName("customer_id")
    var customerId: String,

    @SerializedName("customer_name")
    var customerName: String,

    @SerializedName("address")
    var address: String,

    @SerializedName("phone_number")
    var phoneNumber: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("payment_date")
    var paymentDate: String,

    @SerializedName("paid_amount")
    var paidAmount: Int // Chuyển đổi thành số nguyên
)
