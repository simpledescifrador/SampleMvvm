package com.jimac.vetclinicapp.data.models

import com.google.gson.annotations.SerializedName

data class Appointment(
    @SerializedName("appointment_id") var appointmentId: Int = 0,
    @SerializedName("appointment_number") var appointmentNumber: String = "",
    @SerializedName("appointment_date") var appointmentDate: String = "",
    @SerializedName("start_time") var startTime: String = "",
    @SerializedName("end_time") var entTime: String = "",
    @SerializedName("date_created") var appointmentDateCreated: String = "",

    @SerializedName("appointment_status")
    var appointmentStatus: AppointmentStatus? = null,
    @SerializedName("appointment_service")
    var appointmentService: ClinicService? = null,
    @SerializedName("pet")
    var pet: Pet? = null,

    @SerializedName("service_type") var serviceType: String = "",
    @SerializedName("service_id") var serviceId: Int = 0,
    @SerializedName("service") var service: String = "",
    @SerializedName("pet_id") var petId: Int = 0,
    @SerializedName("pet_name") var petName: String = "",
    @SerializedName("time_slot") var timeSlot: String = "",
    @SerializedName("duration") var duration: Int = 0,
    @SerializedName("note") var note: String = ""

)