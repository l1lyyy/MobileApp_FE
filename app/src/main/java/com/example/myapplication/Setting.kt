package com.example.myapplication
import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("min_import_amount")
    val minImportAmount: Int,

    @SerializedName("max_import_stock")
    val maxImportStock: Int,

    @SerializedName("max_debt")
    val maxDebt: Double,

    @SerializedName("min_stock")
    val minStock: Int,

    @SerializedName("payment_bill_limit")
    val paymentBillLimit: Boolean
)
