package com.jimac.vetclinicapp.data.network.payloads

import com.google.gson.annotations.SerializedName

class GetTimeSlotsPayload(
    @SerializedName("appointment_date") val appointmentDate: String,
    @SerializedName("service_type_id") val serviceTypeId: Int,
    @SerializedName("service_id") val serviceId: Int
)