package com.example.myapplication

import android.media.session.MediaSession.Token
import com.google.gson.annotations.SerializedName

data class User(
    var username: String,
    var email: String,
    var password: String,
    @SerializedName("confirm_password")
    var confirmPassword: String,
)

data class Signin(
    val username: String,
    val password: String,
)

data class Token(
    val token: String
)

