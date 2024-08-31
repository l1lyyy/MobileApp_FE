package com.example.myapplication

data class User(
    var username: String,
    var email: String,
    var password: String,
<<<<<<<< HEAD:app/src/main/java/com/example/myapplication/user_sign_up.kt
    var confirm_password: String,
    var token: String
========
    var confirmPassword: String,
    var token: String
)

data class Signin(
    val username: String,
    val password: String
>>>>>>>> Nhu:app/src/main/java/com/example/myapplication/UserDataClass.kt
)
