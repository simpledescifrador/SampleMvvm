package com.jimac.vetclinicapp.data.network

import com.jimac.vetclinicapp.data.models.Appointment
import com.jimac.vetclinicapp.data.models.Client
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.data.models.TimeSlot
import com.jimac.vetclinicapp.data.network.data_responses.AddAppointmentResponse
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

    abstract suspend fun addAppointment(appointment: Appointment): ResponseWrapper<AddAppointmentResponse>

    abstract suspend fun getClientAppointments(clientId: Int): ResponseWrapper<ArrayList<Appointment>>

    abstract suspend fun getClientPets(clientId: Int): ResponseWrapper<ArrayList<Pet>>
}