package com.saned.view.error


import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorInterceptor  : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+ prefHelper.getBearerToken())
            .addHeader("Accept","application/json").build()

        val response = chain.proceed(request)


        if (!response.isSuccessful) {

            throw SANEDError(response.code(), response.message(), "" + response.body()!!.string())
        }
        return response
    }
}