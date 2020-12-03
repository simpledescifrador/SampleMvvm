package com.jimac.vetclinicapp.data.network.payloads

import com.google.gson.annotations.SerializedName

class LoginPayload(
    @SerializedName("email_address") val emailAddress: String,
    @SerializedName("password") val password: String
)