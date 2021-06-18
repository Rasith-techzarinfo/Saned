package com.saned.model

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.saned.view.error.ErrorInterceptor
import com.saned.view.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    // USE DEFERRED LIST for COROUTINES

    @POST("v1/api/user/login")
    @FormUrlEncoded
    fun performLogin(
        @FieldMap values: HashMap<String, String>
    ): Deferred<UserLoginData>

    @POST( "v1/api/user/fcm-update")
    @FormUrlEncoded
    fun updateFcmToken(
            @FieldMap values: java.util.HashMap<String, String>

    ): Deferred<ResData>

    @GET("v1/api/user/profile")
    fun getProfileData(

    ): Deferred<ProfileData>

    @POST( "v1/api/user/profile/update")
    @Multipart
    fun editProfile(
            @Part("fnme") fnme: RequestBody,
            @Part("lnme") lnme: RequestBody,
            @Part("mnme") mnme: RequestBody,
            @Part("a_name") a_name: RequestBody,
            @Part("dob") dob: RequestBody,
            @Part("gend") gend: RequestBody,
            @Part("ccty") ccty: RequestBody,
            @Part("email") email: RequestBody,
            @Part("phon") phon: RequestBody,
            @Part("relg") relg: RequestBody,
            @Part("emrcnt") emrcnt: RequestBody,
            @Part("emp_code") emp_code: RequestBody,
            @Part("f_name") f_name: RequestBody,
            @Part("dept") dept: RequestBody,
            @Part("jbtl") jbtl: RequestBody,
            @Part("basic") basic: RequestBody,
            @Part("hous") hous: RequestBody,
            @Part("ldate") ldate: RequestBody,
            @Part("grade") grade: RequestBody,
            @Part("idno") idno: RequestBody,
            @Part("idex") idex: RequestBody,
            @Part("pspt") pspt: RequestBody,
            @Part("psptex") psptex: RequestBody,
            @Part("subdep") subdep: RequestBody,
            @Part("id") id: RequestBody,
            @Part("mart") mart: RequestBody,
            @Part("city") city: RequestBody,
            @Part("loca") loca: RequestBody,
            @Part("tran") tran: RequestBody,
            @Part("cont") cont: RequestBody,
            @Part("medc") medc: RequestBody

    ): Deferred<ProfileList>

    @POST("v1/api/user/reset-password")
    @FormUrlEncoded
    fun RestPassword(
            @FieldMap values: java.util.HashMap<String, String>
    ): Deferred<ForgetPasswordData>

    @POST("v1/api/user/check-reset-token")
    @FormUrlEncoded
    fun checkOTP(
            @FieldMap values: java.util.HashMap<String, String>
    ): Deferred<UserLoginData>


    @GET("v1/api/user/employee-list")
    fun getEmployeeList(

    ): Deferred<MyEmployeeList>


    @GET("v1/api/workflow/histroy")
    fun getPendingHistory(

    ): Deferred<Pendinghistory>


    @GET("v1/api/workflow/details/{id}")
    fun getPendingHistoryDetail(
            @Path("id") id: String,

            ): Deferred<PendinghistoryDetail>


    @GET("v1/api/workflow/histroy/{id}")
    fun getPendingServicesDetail(
            @Path("id") id: String,

            ): Deferred<ServiceListDetail>



    @GET("v1/api/workflow/menu-list")
    fun getServicesList(

    ): Deferred<ServicesList>


    @GET("v1/api/dashboard/get-tiles/{id}")
    fun getDashboardDetail(
            @Path("id") id: String,

    ): Deferred<Dashboard>


    @GET("v1/api/attendance/get-location/{id}")
    fun getEmpLocation(
            @Path("id") id: String,

            ): Deferred<GetempLocation>



    @POST("v1/api/attendance/store")
    @FormUrlEncoded
    fun attendancePunch(
            @FieldMap values: HashMap<String, String>
    ): Deferred<AttendanceStore>


    @POST("v1/api/attendance/history")
    @FormUrlEncoded
    fun attendanceSearch(
            @FieldMap values: HashMap<String, String>
    ): Deferred<AttendanceHistory>


    @GET("v1/api/workflow/get-fields/{id}")
    fun getDynamicForm(
            @Path("id") id: String,

            ): Deferred<DynamicForm>



    ////////////////////////////









    //user
    @GET("v1/api/workflow/housing-advance/list")
    fun getHousingWFListUser(

    ): Deferred<HousingWFList>

    //manager 1
    @GET("v1/api/workflow/housing-advance/pending/list")
    fun getHousingWFListManager1(

    ): Deferred<HousingWFList>

    //manager 2
    @GET("v1/api/workflow/housing-advance/first-approved/list")
    fun getHousingWFListManager2(

    ): Deferred<HousingWFList>

    @GET("v1/api/workflow/housing-advance/{id}")
    fun getHousingWFDetail(
         @Path("id") id: String,

    ): Deferred<HADetailData>

    @POST("v1/api/workflow/housing-advance/create")
    @Multipart
    fun sendHA(
        @Part("Reason") reason: RequestBody,
        @Part("Month No") months: RequestBody,
        @Part("User ID") userID: RequestBody,
        @Part files: ArrayList<MultipartBody.Part>?
    ): Deferred<UserLoginData>

    //manager 1
    @POST( "v1/api/workflow/housing-advance/verify")
    @FormUrlEncoded
    fun verifyHAStatus1(
            @FieldMap values: java.util.HashMap<String, String>

    ): Deferred<UserLoginData>

    //manager 2
    @POST( "v1/api/workflow/housing-advance/main/verify")
    @FormUrlEncoded
    fun verifyHAStatus2(
            @FieldMap values: java.util.HashMap<String, String>

    ): Deferred<UserLoginData>


    companion object {
        fun create(): ApiService {

            val gson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .setLenient()
                .create()

            //   val apiClient = OkHttpClient().newBuilder().addInterceptor(ErrorInterceptor()).build()

            val apiClient = OkHttpClient().newBuilder().readTimeout(300, TimeUnit.SECONDS).writeTimeout(300,
                TimeUnit.SECONDS).addInterceptor(ErrorInterceptor()).build()


            val retrofit = Retrofit.Builder()
                .client(apiClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.BASE_URL)
                .build()


            return retrofit.create(ApiService::class.java)
        }
    }
}