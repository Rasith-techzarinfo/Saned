package com.saned.model

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nchores.user.model.UserLoginData
import com.saned.view.error.ErrorInterceptor
import com.saned.view.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    // USE DEFERRED LIST for COROUTINES

    @POST("backend-neighbourhood/public/api/userLogin")
    @FormUrlEncoded
    fun performLogin(
        @FieldMap values: HashMap<String, String>
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