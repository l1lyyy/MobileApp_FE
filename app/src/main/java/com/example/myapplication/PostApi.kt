package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PostApi {
    companion object {
        const val ROOT = "http://192.168.1.102:8000/"
        //const val BASE_LOCAL = "${ROOT}api/v1/"
        const val BASE_URL = "${ROOT}account/"
        //const val POST_URL = "${BASE_LOCAL}post/"
        const val API_URL = ROOT + "api/v1/"
    }
    @POST("register/")
    fun registrationUser(@Body userModel: User): Call<User>
}
