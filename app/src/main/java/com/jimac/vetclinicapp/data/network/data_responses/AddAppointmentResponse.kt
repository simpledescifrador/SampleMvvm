package com.jimac.vetclinicapp.data.network.data_responses

import com.google.gson.annotations.SerializedName

class AddAppointmentResponse(
    @SerializedName("appointment_number") val appointmentNumber: String
)