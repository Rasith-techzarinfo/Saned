package com.saned.model

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.saned.view.error.ErrorInterceptor
import com.saned.view.utils.Constants
import io.reactivex.Observable
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

    ): Deferred<UserEditProfileData>

    @POST( "v1/api/user/profile/update")
    @FormUrlEncoded
    fun editProfile(
            @FieldMap values: java.util.HashMap<String, String>

    ): Deferred<UserLoginData>

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


    @GET("v1/api/workflow/menu-list")
    fun getServicesList(

    ): Deferred<ServicesList>


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