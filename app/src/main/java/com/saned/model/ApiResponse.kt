package com.nchores.user.model


data class UserLoginData (

    val status : String,
    val message : String,
    val userdetails : UserData?,
    val token : String,
    val errors: LoginError?
)

data class UserData (

    val id : String,
    val username : String,
    val email : String,
    val phone : String,
    val gender : String,
    val password : String,
    val device_type : String,
    val otp : String,
    val otp_expire_at : String,
    val created_at : String,
    val updated_at : String
)

data class LoginError(
    val email: ArrayList<String>?,
    val password: ArrayList<String>?
)

//FIXME just for demo
data class SampleCatogeries(
    val title: String,
    var thumbnail: String,
    val subtitle: String
)
