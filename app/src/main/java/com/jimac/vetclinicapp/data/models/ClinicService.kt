package com.jimac.vetclinicapp.data.models

import com.google.gson.annotations.SerializedName

data class ClinicService(
    @SerializedName("service_id") val serviceId: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("duration") val duration: Int  = 0,
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("date_created") val dateCreated: String = "",
    @SerializedName("clinic_id") val clinicId: Int = 0,
    @SerializedName("service_type_id") val serviceTypeId: Int = 0,
    @SerializedName("service") val service: String = ""
)