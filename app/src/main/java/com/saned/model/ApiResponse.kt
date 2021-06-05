package com.saned.model

import java.text.FieldPosition


data class UserLoginData (

    val success : String,
    val message : String,
    val token : String,
    val user : UserData?,
    val errors: LoginError?
)
data class UserEditProfileData(
        val emp_code : String,
        val f_name : String,
        val a_name : String,
        val dept : String,
        val jbtl : String,
        val join : String,
        val dob : String,
        val ccty : String,
        val email : String,
        val phon : String,
        val iban : String,
        val mngr : String,
        val basic : String,
        val hous : String,
        val ldate : String,
        val gend : String,
        val grade : String,
        val idno : String,
        val relg : String,
        val days : String,
        val gosi : String,
        val cash : String,
        val fnme : String,
        val lnme : String,
        val mnme : String,
        val prof : String,
        val ovrt : String,
        val idex : String,
        val pspt : String,
        val psptex : String,
        val cnttyp : String,
        val emrcnt : String,
        val gosino : String,
        val cntrex : String,
        val subdep : String,
        val proj : String,
        val id : String,

)

data class UserData (

    val uuid : String,
    val t_idno : String,
    val t_emno : String,
    val t_nama : String,
    val t_lnme : String,
    val t_comp : String,
    val t_pass : String,
    val t_mail : String,
    val t_role : String,
    val t_page : String,
    val is_active : String,
    val fcm_token : String,
    val remember_token : String,
    val remember_token_at : String,
    val profile_pic : String,
    val created_by : String,
    val updated_by : String,
    val createdAt : String,
    val updatedAt : String,
    val deletedAt : String,
    val company : Company,
    val role_name : String


//        val uuid : String,
//        val t_idno : String,
//        val t_emno : String,
//        val t_nama : String,
//        val t_comp : String,
//        val t_pass : String,
//        val t_mail : String,
//        val t_role : String,
//        val t_page : String,
//        val is_active : String,
//        val fcm_token : String,
//        val created_by : String,
//        val updated_by : String,
//        val createdAt : String,
//        val updatedAt : String,
//        val deletedAt : String

//    val id : String,
//    val uuid : String,
//    val first_name : String,
//    val last_name : String,
//    val email : String,
//    val user_name : String,
//    val password : String,
//    val phone : String,
//    val company_id : String,
//    val role_id : String,
//    val previous_login : String,
//    val parent : String,
//    val is_active : String,
//    val created_by : String,
//    val updated_by : String,
//    val createdAt : String,
//    val updatedAt : String,
//    val deletedAt : String,
//    val roleId : String,
//    val companyId : String

)



data class Company (

    val id : String,
    val nama : String,
    val namc : String,
    val ccty : String,
    val name : String,
    val vath : String,
    val vatn : String,
    val logo : String,
    val tybu : String,
    val scbu : String,
    val pdat : String,
    val tbr1 : String,
    val tbn1 : String,
    val tdp1 : String,
    val cops : String,
    val scp1 : String,
    val prli : String,
    val cwar : String,
    val clot : String,
    val cloc : String,
    val status : String,
    val createdAt : String,
    val updatedAt : String,
    val deletedAt : String
)

data class LoginError(
    val email: ArrayList<String>?,
    val password: ArrayList<String>?
)

data class ResData(
        val success: String,
        val message: String
)
data class ForgetPasswordData (
        val success : String,
        val message : String,
        val data : UserData
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