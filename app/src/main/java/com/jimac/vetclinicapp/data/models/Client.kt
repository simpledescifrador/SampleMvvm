package com.jimac.vetclinicapp.data.models

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("user_id") var userId: Int? = 0,
    @SerializedName("date_created") var dateCreated: String? = "",
    @SerializedName("client_id") var clientId: Int? = 0,

    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("address") var address: String,
    @SerializedName("contact_number") var contactNumber: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("email_address") var email: String,
    @SerializedName("password") var password: String
)