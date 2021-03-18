package com.saned.view.error

import java.io.IOException

class SANEDError constructor(private val responseCode: Int, private val messageVal: String, private val errorResponse:String) : IOException() {

    private val TAG = "SANEDError"

    fun getResponseCode(): Int {
        return responseCode
    }

    fun getErrorMessage(): String {
        return messageVal
    }
    fun getErrorResponse(): String {
        return errorResponse
    }
}