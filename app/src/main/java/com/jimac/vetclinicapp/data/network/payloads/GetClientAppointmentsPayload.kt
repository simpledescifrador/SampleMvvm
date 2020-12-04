package com.jimac.vetclinicapp.data.network.payloads

import com.google.gson.annotations.SerializedName

class GetClientAppointmentsPayload(@SerializedName("client_id") val clientId: Int)