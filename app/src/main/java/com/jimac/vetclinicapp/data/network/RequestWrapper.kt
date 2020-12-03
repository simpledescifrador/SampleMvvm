package com.jimac.vetclinicapp.data.network

import com.google.gson.annotations.SerializedName

class RequestWrapper<T>(@SerializedName("event") val event: String, @SerializedName("payload") val payload: T?)