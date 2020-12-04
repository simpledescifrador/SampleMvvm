package com.jimac.vetclinicapp.data.network

import com.jimac.vetclinicapp.data.models.Appointment
import com.jimac.vetclinicapp.data.models.Client
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.data.models.TimeSlot
import com.jimac.vetclinicapp.data.network.data_responses.AddAppointmentResponse
import com.jimac.vetclinicapp.data.network.data_responses.AddPetResponse
import com.jimac.vetclinicapp.data.network.data_responses.GetNewSchedDataResponse
import com.jimac.vetclinicapp.data.network.data_responses.GetPetDataResponse
import com.jimac.vetclinicapp.data.network.payloads.EmptyPayload
import com.jimac.vetclinicapp.data.network.payloads.GetClientAppointmentsPayload
import com.jimac.vetclinicapp.data.network.payloads.GetClientPetsPayload
import com.jimac.vetclinicapp.data.network.payloads.GetTimeSlotsPayload
import com.jimac.vetclinicapp.data.network.payloads.LoginPayload
import com.jimac.vetclinicapp.data.network.payloads.NewScheduleDataPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun registerClient(@Body requestBody: RequestWrapper<Client>): ResponseWrapper<Client>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun login(@Body payloadBody: RequestWrapper<LoginPayload>): ResponseWrapper<Client>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun loadPetData(@Body requestBody: RequestWrapper<EmptyPayload>): ResponseWrapper<GetPetDataResponse>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun registerPet(@Body requestBody: RequestWrapper<Pet>): ResponseWrapper<AddPetResponse>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun loadNewSchedData(@Body requestBody: RequestWrapper<NewScheduleDataPayload>): ResponseWrapper<GetNewSchedDataResponse>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun searchAvailableSlots(@Body requestBody: RequestWrapper<GetTimeSlotsPayload>): ResponseWrapper<ArrayList<TimeSlot>>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun addAppointment(@Body requestBody: RequestWrapper<Appointment>): ResponseWrapper<AddAppointmentResponse>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun getClientAppointments(@Body requestBody: RequestWrapper<GetClientAppointmentsPayload>): ResponseWrapper<ArrayList<Appointment>>

    @POST("AKfycbwrpEgXPz4NrqjDVIVMb6-otuni8gAb-q93bmUrnGbgWoavmrB8/exec")
    suspend fun getClientPets(@Body requestBody: RequestWrapper<GetClientPetsPayload>): ResponseWrapper<ArrayList<Pet>>
}