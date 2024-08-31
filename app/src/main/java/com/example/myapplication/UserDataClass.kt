package com.example.myapplication

data class User(
    var username: String,
    var email: String,
    var password: String,
    var confirmPassword: String,
    var token: String
)

data class Signin(
    val username: String,
    val password: String
)
