package com.jimac.vetclinicapp.data.network.data_responses

import com.google.gson.annotations.SerializedName
import com.jimac.vetclinicapp.data.models.ClinicService
import com.jimac.vetclinicapp.data.models.Pet

class GetNewSchedDataResponse(
    @SerializedName("service_types") val serviceTypes: ArrayList<String>,
    @SerializedName("clinic_services") val clinicServices: HashMap<String, ArrayList<ClinicService>>,
    @SerializedName("pets") val clientPets: ArrayList<Pet>
)