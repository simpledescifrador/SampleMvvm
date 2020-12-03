package com.jimac.vetclinicapp.data.network.data_responses

import com.google.gson.annotations.SerializedName

class AddPetResponse(
    @SerializedName("pet_id")
    val petId: Int
)