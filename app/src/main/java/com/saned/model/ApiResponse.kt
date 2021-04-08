package com.saned.model

import java.text.FieldPosition


data class UserLoginData (

    val success : String,
    val message : String,
    val token : String,
    val user : UserData?,
    val errors: LoginError?
)

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


//FIXME dummy class  \\
data class ServicesMenu(
    val title: String,
    var id: String
)


data class NotifyData(
        val id: String,
        val readStatus: String,
        val title: String,
        val message: String,
        var timeAgo: String,
        var type: String,
        var wkid: String,
//        var wkname: String,
        var name: String,
        var profile: String
)

data class EmployeeData(
    val id: String,
    val name: String,
    val designation: String,
    val email: String,
    var number: String,
    var profile: String
)

data class AttendenceData(
        val id: String,
        val date: String,
        val timein: String,
        val timeout: String,
        var wokringHrs: String
)

//FIXME end dummmy class  \\

data class HAData(
        val noofmonths: String,
        val reason: String,
        val userID: String,
        val document: String,
        var id: String
)


data class HousingWFData (

        val id : String,
        val wkid : String,
        val sern : String,
        val labl : String,
        val data : String,
        val form_name : String,
        val email : String
)

data class HAData1(
        val position: Int,
        val wkid: String
)

data class HousingWFList (

        val success : String,
        val message : String,
        val data : List<HousingWFData>?
)

data class HADetailData (

        val success : String,
        val message : String,
        val data : List<HousingWFData>?,
        val approvalstatus : HAApprovalStatus?,
        val history : List<HAApprovalStatus>?
)

data class HAApprovalStatus (

        val id : String,
        val wkid : String,
        val type : String,
        val emno : String,
        val date : String,
        val time : String,
        val step : String,
        val stnm : String,
        val nemn : String,
        val ndat : String,
        val ntim : String
)