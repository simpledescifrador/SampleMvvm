package com.jimac.vetclinicapp.data.network

import com.google.gson.annotations.SerializedName

class ResponseWrapper<T> {

    @SerializedName("data")
    val data: T? = null
    @SerializedName("error_code")
    val errorCode = 0
    @SerializedName("message")
    val message = ""
    @SerializedName("status")
    val status = 0
}