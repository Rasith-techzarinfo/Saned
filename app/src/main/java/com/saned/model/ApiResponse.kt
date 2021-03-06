package com.saned.model

import java.text.FieldPosition


data class UserLoginData (

    val success : String,
    val message : String,
    val token : String,
    val user : UserData?,
    val errors: LoginError?,
)

data class ProfileData (

        val success : String,
        val user : List<Profile>,
        val message : String
)

data class Profile (

    val sno : String,
    val id : String,
    val emp_code : String,
    val f_name : String,
    val a_name : String,
    val dept : String,
    val jbtl : String,
    val stat : String,
    val dob : String,
    val ccty : String,
    val email : String,
    val password : String,
    val phon : String,
    val mart : String,
    val bank : String,
    val city : String,
    val loca : String,
    val basic : String,
    val hous : String,
    val tran : String,
    val bnka : String,
    val cont : String,
    val medc : String,
    val ldate : String,
    val gend : String,
    val grade : String,
    val idno : String,
    val relg : String,
    val vacb : String,
    val cash : String,
    val refcntd : String,
    val refcntu : String,
    val fnme : String,
    val lnme : String,
    val mnme : String,
    val idex : String,
    val pspt : String,
    val psptex : String,
    val emrcnt : String,
    val subdep : String,
    val created_at : String,
    val updated_at : String,
    val deleted_at : String,
    val medical_class : String,
    val department_name : String,
    val sub_name : String,
    val job_title : String,
    val grade_name : String,
    val location_name : String
)
data class ProfileList (

        val success : String,
        val message : String,
        val data : UserEditProfileData?
)
data class UserEditProfileData(
        val emp_code : String,
        val f_name : String,
        val a_name : String,
        val dept : String,
        val jbtl : String,
        val dob : String,
        val ccty : String,
        val email : String,
        val phon : String,
        val basic : String,
        val hous : String,
        val ldate : String,
        val gend : String,
        val grade : String,
        val idno : String,
        val relg : String,
        val cash : String,
        val fnme : String,
        val lnme : String,
        val mnme : String,
        val idex : String,
        val pspt : String,
        val psptex : String,
        val emrcnt : String,
        val subdep : String,
        val id : String,
        val mart : String,
        val city : String,
        val loca : String,
        val tran : String,
        val cont : String,
        val medc : String


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


data class Pendinghistory (

        val success : String,
        val message : String,
        val data : List<Data>
)

data class Data (

        val wkid : String,
        val form_name : String,
        val pending_since : String,
        val pending_with : String,
        val added_by : String,
        val added_at : String,
        val profile : String,
        val job_title : String,
        val last_action_date : String,
        val status : String,
        val reason : String
)


data class PendinghistoryDetail (

    val success : String,
    val message : String,
    val data : List<PendingDetail>,
    val status : String
)

data class PendingDetail (

    val id : String,
    val wkid : String,
    val sern : String,
    val labl : String,
    val data : String,
    val form_name : String,
    val email : String,
    val added_by : String,
    val added_at : String,
    val t_idno : String,
    val emp_name : String,
    val t_emno : String,
    val emp_code : String
)



data class MyEmployeeList (

        val success : String,
        val message : String,
        val data : List<Empdata>?
)
data class Empdata (

        val t_idno : String,
        val uuid : String,
        val t_mail : String,
        val t_nama : String,
        val t_pass : String,
        val t_lnme : String,
        val t_comp : String,
        val t_role : String,
        val t_page : String,
        val t_emno : String,
        val is_active : String,
        val created_by : String,
        val updated_by : String,
        val profile_pic : String,
        val created_at : String,
        val updated_at : String,
        val deleted_at : String,
        val fcm_token : String,
        val remember_token : String,
        val remember_token_at : String,
        val emp_id : String,
        val f_name : String,
        val a_name : String,
        val jbtl : String,
        val email : String,
        val job_title : String
)

data class ServicesList (

        val success : String,
        val message : String,
        val data : List<ServList>
)


data class ServList (

        val id : String,
        val module_name : String,
        val slug : String,
        val icon : String,
        val package_id : String,
        val addon_id : String,
        val parent : String,
        val menu_order : String,
        val user_privillage : String,
        val dynamic : String,
        val createdAt : String,
        val updatedAt : String,
        val deletedAt : String,
        val policy_id : String
)

data class ServiceListDetail (

        val success : String,
        val message : String,
        val data : List<ServDetail>,
        val status : String
)

data class ServDetail (

        val id : String,
        val wkid : String,
        val sern : String,
        val labl : String,
        val data : String,
        val form_name : String,
        val email : String,
        val added_by : String,
        val added_at : String
)

data class Dashboard (

        val success : String,
        val message : String,
        val data : List<DashboardDetail>
)

data class DashboardDetail (

        val emp_code : String,
        val basic : String,
        val net : String,
        val earnings : String,
        val deduction : String,
        val period : String,
        val id : String,
        val vacb : String

)


data class GetempLocation (

        val success : String,
        val message : String,
        val location : String,
        val radius : String
)


data class AttendanceStore(

        val success : String,
        val message : String,
        val data : List<StoreData>
)


data class StoreData (

        val id : String,
        val location : String,
        val in_time : String,
        val out_time : String,
        val emp_id : String,
        val date : String,
        val working_hours : String
)

data class AttendanceHistory (

        val success : String,
        val message : String,
        val data : List<AttenHistoryDetail>
)

data class AttenHistoryDetail (

        val id : String,
        val location : String,
        val in_time : String,
        val out_time : String,
        val emp_id : String,
        val date : String,
        val working_hours : String
)

data class DynamicForm  (

    val success : String,
    val message : String,
    val data : List<DynamicFormList>
)

data class DynamicFormList (

    val id : String,
    val workflow_id : String,
    val type : String,
    val name : String,
    val sern : String,
    val modu : String,
    val list : String
)


data class LeaveType (

    val success : String,
    val message : String,
    val data : List<LeaveList>

        )

data class LeaveList (

    val name: String,
    val type: String

        )

data class RequestUpdate(

    val success : String,
    val message : String,
    val data : String
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


data class AttendenceData(
        val id: String,
        val date: String,
        val timein: String,
        val timeout: String,
        var wokringHrs: String
)

//FIXME end dummmy class  \\
data class DynamicData(
        val success: String,
        val message: String,
        val data: List<Dynamic>?
)
data class Dynamic(
    val id: String
)

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