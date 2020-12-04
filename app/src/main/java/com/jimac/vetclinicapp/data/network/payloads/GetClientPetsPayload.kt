package com.jimac.vetclinicapp.data.network.payloads

import com.google.gson.annotations.SerializedName

class GetClientPetsPayload(@SerializedName("client_id") val clientId: Int)