package com.jimac.vetclinicapp.data.network.data_responses

import com.google.gson.annotations.SerializedName

class GetPetDataResponse(
    @SerializedName("species") val petSpecies: ArrayList<String>,
    @SerializedName("breeds") val petBreeds: HashMap<String, ArrayList<String>>,
    @SerializedName("sizes") val petSize: ArrayList<String>
)

