package com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.data.models.Appointment
import com.jimac.vetclinicapp.data.models.ClinicService
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.ui.base.BaseViewModel
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ActionState.ProceedToSubmitAppointment
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.ChangeServicesList
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.Error
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.FormError
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.InitLoading
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.Loading
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.OnInitLoaded
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.SetAvailableTimeSlots
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.SetServiceDuration
import com.jimac.vetclinicapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class NewAppointmentViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NewAppointmentViewModel(mAppDataManager) as T
        }
    }

    private var serviceType = ArrayList<String>()
    private var servicesMap = HashMap<String, ArrayList<ClinicService>>()
    private var pets = ArrayList<Pet>()
    private var selectedServices = ArrayList<ClinicService>()

    fun validateForm(appointment: Appointment) {
        var formValidation = true

        var serviceTypeError: String? = null
        var clinicServiceError: String? = null
        var petError: String? = null
        var appointmentDateError: String? = null
        var timeSlotError: String? = null

        //Service Type Validations
        if (appointment.serviceType.isEmpty()) {
            serviceTypeError = "Service category is required"
            formValidation = false
        }

        //Clinic Services Validations
        if (appointment.service.isEmpty()) {
            clinicServiceError = "Required field"
            formValidation = false
        } else if (appointment.serviceType.isNotEmpty()) {
            var isFound = false
            for ((key, _) in servicesMap) {
                if (key == appointment.serviceType) {
                    isFound = servicesMap[key]?.any {
                        it.title == appointment.service
                    }!!
                }
            }

            if (!isFound) {
                clinicServiceError = "Invalid service"
                formValidation = false
            }
        }

        //Pet Validations
        if (appointment.petName.isEmpty()) {
            petError = "Required field"
            formValidation = false
        } else if (appointment.petName.isNotEmpty()) {
            var isFound = false
            pets.forEach { pet ->
                if (pet.name == appointment.petName) {
                    isFound = true
                }
            }

            if (!isFound) {
                petError = "Invalid pet"
                formValidation = false
            }
        }

        //Appointment Date Validations
        if (appointment.appointmentDate.isEmpty()) {
            appointmentDateError = "Required field"
            formValidation = false
        }

        //Time Slot Validations
        if (appointment.timeSlot.isEmpty()) {
            timeSlotError = "Time Slot is required"
            formValidation = false
        }

        viewState.value = FormError(
            serviceTypeError,
            clinicServiceError,
            petError,
            appointmentDateError,
            timeSlotError
        )

        if (formValidation) {
            actionState.value = ProceedToSubmitAppointment(appointment)
        }
    }

    fun onChangeServices(serviceType: String) {
        viewState.value = servicesMap[serviceType]?.let {
            selectedServices = it

            val servicesListString = arrayListOf<String>()

            it.forEach { clinicService ->
                servicesListString.add(clinicService.title)
            }
            ChangeServicesList(servicesListString)
        }
    }

    fun onSelectedService(service: String) {
        var duration = "Service Duration: "
        selectedServices.forEach {
            if (service == it.title) {
                duration += "${it.duration} mins"
            }
        }

        viewState.value = SetServiceDuration(duration)
    }

    fun init() {
        viewModelScope.launch {
            val clinicId = 1 //Belly Rub Clinic
            val clientId = appDataManager.sharedPreferenceManager.clientData.clientId
            viewState.postValue(InitLoading)
            clientId?.let {
                appDataManager.networkRepository.loadNewSchedData(clinicId, it).run {
                    if (status == 0) {
                        if (errorCode != 0) {
                            when (errorCode) {
                                99 -> {
                                    //Internal Error 505;
                                    Log.e("NewAppointViewModel", message)
                                }
                            }
                        }
                        viewState.postValue(Error(message))
                    } else {
                        data?.let { response ->
                            pets = response.clientPets
                            servicesMap = response.clinicServices
                            serviceType = response.serviceTypes

                            val petNamesString = arrayListOf<String>()

                            pets.forEach { pet ->
                                petNamesString.add(pet.name)
                            }

                            viewState.postValue(
                                OnInitLoaded(
                                    serviceType,
                                    petNamesString
                                )
                            )
                        }
                    }

                }
            }
        }
    }

    fun searchAvailableSlots(serviceType: String, serviceTitle: String, appointmentDate: String) {
        viewState.value = Loading
        var serviceTypeId = 0
        var serviceId = 0
        if (serviceType == "Pet Grooming") {
            serviceTypeId = 1
        } else if (serviceType == "Clinic") {
            serviceTypeId = 2
        }
        for ((_, value) in servicesMap) {
            value.forEach {
                if (serviceTitle == it.title) {
                    serviceId = it.serviceId
                }
            }
        }

        viewModelScope.launch {
            appDataManager.networkRepository.searchAvailableSlots(appointmentDate, serviceTypeId, serviceId).run {
                if (status == 0) {
                    if (errorCode != 0) {
                        when (errorCode) {
                            99 -> {
                                //Internal Error 505;
                                Log.e("NewAppointViewModel", message)
                            }
                        }
                    }
                    viewState.postValue(Error(message))
                } else {
                    data?.let { response ->
                        val slots = arrayListOf<String>()
                        response.forEach {
                            slots.add(it.toString())
                        }
                        viewState.postValue(SetAvailableTimeSlots(slots))

                    }
                }

            }
        }
    }

    val viewState = MutableLiveData<ViewState>()
    val actionState = SingleLiveEvent<ActionState>() // each value emit only once

    sealed class ViewState {
        object Loading : ViewState()
        object InitLoading : ViewState()
        data class Error(var errorMessage: String) : ViewState()
        data class OnInitLoaded(
            var serviceType: ArrayList<String>,
            var petNames: ArrayList<String>
        ) : ViewState()

        data class ChangeServicesList(var services: ArrayList<String>) : ViewState()
        data class SetServiceDuration(val duration: String) : ViewState()
        data class FormError(
            var serviceTypeError: String?,
            var servicesError: String?,
            var petError: String?,
            var appointmentDateError: String?,
            var timeSlotsError: String?
        ) : ViewState()

        data class SetAvailableTimeSlots(val timeSlots: ArrayList<String>) : ViewState()
    }

    sealed class ActionState {
        data class ProceedToSubmitAppointment(var appointment: Appointment) : ActionState()
    }
}