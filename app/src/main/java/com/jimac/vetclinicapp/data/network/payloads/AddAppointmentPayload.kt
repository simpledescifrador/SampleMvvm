package com.jimac.vetclinicapp.data.network.payloads

import com.google.gson.annotations.SerializedName

class AddAppointmentPayload(
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("appointment_date") val appointmentDate: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("note") val note: String,
    @SerializedName("pet_id") val petId: Int
)