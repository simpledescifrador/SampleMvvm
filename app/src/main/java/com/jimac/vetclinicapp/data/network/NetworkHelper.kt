package com.jimac.vetclinicapp.data.network

import com.jimac.vetclinicapp.data.models.Client
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.data.models.TimeSlot
import com.jimac.vetclinicapp.data.network.data_responses.AddPetResponse
import com.jimac.vetclinicapp.data.network.data_responses.GetNewSchedDataResponse
import com.jimac.vetclinicapp.data.network.data_responses.GetPetDataResponse

abstract class NetworkHelper {

    abstract suspend fun registerClient(client: Client?): ResponseWrapper<Client>

    abstract suspend fun login(emailAddress: String, password: String): ResponseWrapper<Client>

    abstract suspend fun loadPetData(): ResponseWrapper<GetPetDataResponse>

    abstract suspend fun registerPet(pet: Pet): ResponseWrapper<AddPetResponse>

    abstract suspend fun loadNewSchedData(clinicId: Int, clientId: Int): ResponseWrapper<GetNewSchedDataResponse>

    abstract suspend fun searchAvailableSlots(
        appointmentDate: String,
        serviceTypeId: Int,
        serviceId: Int
    ): ResponseWrapper<ArrayList<TimeSlot>>
}