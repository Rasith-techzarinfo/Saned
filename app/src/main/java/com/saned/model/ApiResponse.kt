package com.saned.model


data class UserLoginData (

    val success : String,
    val message : String,
    val token : String,
    val user : UserData?,
    val errors: LoginError?
)

//data class UserData (
//
//    val id : String,
//    val username : String,
//    val email : String,
//    val phone : String,
//    val gender : String,
//    val password : String,
//    val device_type : String,
//    val otp : String,
//    val otp_expire_at : String,
//    val created_at : String,
//    val updated_at : String
//)

data class UserData (

    val id : String,
    val uuid : String,
    val first_name : String,
    val last_name : String,
    val email : String,
    val user_name : String,
    val password : String,
    val phone : String,
    val company_id : String,
    val role_id : String,
    val previous_login : String,
    val parent : String,
    val is_active : String,
    val created_by : String,
    val updated_by : String,
    val createdAt : String,
    val updatedAt : String,
    val deletedAt : String,
    val roleId : String,
    val companyId : String
)

data class LoginError(
    val email: ArrayList<String>?,
    val password: ArrayList<String>?
)

data class ResData(

        val success: String,
        val message: String
)


//FIXME just for demo
data class ServicesMenu(
    val title: String,
    var id: String
)

data class HAData(
    val noofdays: String,
    val reason: String,
    var id: String
)
