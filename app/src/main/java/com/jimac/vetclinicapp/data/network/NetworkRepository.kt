package com.jimac.vetclinicapp.data.network

import com.jimac.vetclinicapp.data.models.Client
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.data.models.TimeSlot
import com.jimac.vetclinicapp.data.network.data_responses.AddPetResponse
import com.jimac.vetclinicapp.data.network.data_responses.GetNewSchedDataResponse
import com.jimac.vetclinicapp.data.network.data_responses.GetPetDataResponse
import com.jimac.vetclinicapp.data.network.payloads.EmptyPayload
import com.jimac.vetclinicapp.data.network.payloads.GetTimeSlotsPayload
import com.jimac.vetclinicapp.data.network.payloads.LoginPayload
import com.jimac.vetclinicapp.data.network.payloads.NewScheduleDataPayload
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkRepository : NetworkHelper() {

    private val mApiService: ApiService by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(BODY)
        val client = Builder().addInterceptor(interceptor).build()
        Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(ApiService::class.java)
    }

    override suspend fun registerClient(client: Client?): ResponseWrapper<Client> {
        return mApiService.registerClient(RequestWrapper("register", client))
    }

    override suspend fun login(emailAddress: String, password: String): ResponseWrapper<Client> {
        return mApiService.login(RequestWrapper("login", LoginPayload(emailAddress, password)))
    }

    override suspend fun loadPetData(): ResponseWrapper<GetPetDataResponse> {
        return mApiService.loadPetData(RequestWrapper("getPetData", EmptyPayload()))
    }

    override suspend fun registerPet(pet: Pet): ResponseWrapper<AddPetResponse> {
        return mApiService.registerPet(RequestWrapper("addNewPet", pet))
    }

    override suspend fun loadNewSchedData(clinicId: Int, clientId: Int): ResponseWrapper<GetNewSchedDataResponse> {
        return mApiService.loadNewSchedData(
            RequestWrapper(
                "loadNewScheduleData",
                NewScheduleDataPayload(clinicId, clientId)
            )
        )
    }

    override suspend fun searchAvailableSlots(
        appointmentDate: String,
        serviceTypeId: Int,
        serviceId: Int
    ): ResponseWrapper<ArrayList<TimeSlot>> {
        return mApiService.searchAvailableSlots(
            RequestWrapper(
                "getAvailableTimeSlots", GetTimeSlotsPayload(
                    appointmentDate, serviceTypeId, serviceId
                )
            )
        )
    }
}