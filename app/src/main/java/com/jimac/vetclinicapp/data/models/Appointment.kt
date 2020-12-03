package com.jimac.vetclinicapp.data.models

import com.google.gson.annotations.SerializedName

data class Appointment(
    @SerializedName("service_type") var serviceType: String = "",
    @SerializedName("service_id") var serviceId: Int = 0,
    @SerializedName("service") var service: String = "",
    @SerializedName("pet_id") var petId: Int = 0,
    @SerializedName("pet_name") var petName: String = "",
    @SerializedName("appointment_date") var appointmentDate: String = "",
    @SerializedName("time_slot") var timeSlot: String = "",
    @SerializedName("note") var note: String = ""
)