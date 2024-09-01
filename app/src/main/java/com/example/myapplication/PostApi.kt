package com.example.myapplication

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PostApi {
    companion object {
        const val ROOT = "http://192.168.1.102:8000/"
        //const val BASE_LOCAL = "${ROOT}api/v1/"
        const val BASE_URL = "${ROOT}account/"
        const val INVOICE_URL = "${ROOT}invoice/"
        const val RECEIPT_URL = "${ROOT}receipt/"
        const val ADD_URL = "${ROOT}book/"
        //const val POST_URL = "${ROOT}post/"
        const val API_URL = ROOT + "api/v1/"
    }
    @POST("add-book/")
    fun sendBookData(@Header("Authorization") authHeader: String, @Body bookData: BookData): Call<ResponseBody>
    @POST("create-receipt/")
    fun sendReceipt(@Header("Authorization") authHeader: String, @Body customerData: CustomerData): Call<ResponseBody>

    @POST("create-invoice/")
    fun sendSalesInvoice(@Header("Authorization") authHeader: String, @Body salesInvoice: Salesinvoice): Call<ResponseBody>

    @POST("register/")
    fun registrationUser(@Body userModel: User): Call<User>

    @POST("api-token-auth/")
    fun signinUser(@Body login: Signin ): Call<Token>
}