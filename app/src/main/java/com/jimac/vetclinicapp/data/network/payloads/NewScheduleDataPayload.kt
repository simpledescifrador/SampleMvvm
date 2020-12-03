package com.jimac.vetclinicapp.data.network.payloads

import com.google.gson.annotations.SerializedName

class NewScheduleDataPayload(
    @SerializedName("clinic_id") val clinicId: Int,
    @SerializedName("client_id") val clientId: Int
)