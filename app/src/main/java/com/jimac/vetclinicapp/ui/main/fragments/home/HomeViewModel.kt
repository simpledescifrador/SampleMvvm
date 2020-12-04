package com.jimac.vetclinicapp.ui.main.fragments.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.data.models.Appointment
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.ui.base.BaseViewModel
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.Error
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.LoadPets
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.LoadRecentAppointments
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.Loading
import com.jimac.vetclinicapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class HomeViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(mAppDataManager) as T
        }
    }

    fun getClientAppointments() {
        val clientId = appDataManager.sharedPreferenceManager.clientData.clientId
        viewState.value = Loading
        viewModelScope.launch {
            if (clientId != null) {
                appDataManager.networkRepository.getClientAppointments(clientId).run {
                    if (status == 0) {
                        if (errorCode != 0) {
                            when (errorCode) {
                                99 -> {
                                    //Internal Error 505;
                                    Log.e("HomeViewModel", message)
                                }
                            }
                        }
                        viewState.postValue(Error(message))
                    } else {
                        data?.let { response ->
                            viewState.postValue(LoadRecentAppointments(response.reversed()))
                        }
                    }
                }
            } else {
                viewState.postValue(Error("Error Occurred.. Please logout then login again."))
            }
        }
    }

    fun getClientPets() {
        val clientId = appDataManager.sharedPreferenceManager.clientData.clientId
        viewState.value = Loading
        viewModelScope.launch {
            if (clientId != null) {
                appDataManager.networkRepository.getClientPets(clientId).run {
                    if (status == 0) {
                        if (errorCode != 0) {
                            when (errorCode) {
                                99 -> {
                                    //Internal Error 505;
                                    Log.e("HomeViewModel", message)
                                }
                            }
                        }
                        viewState.postValue(Error(message))
                    } else {
                        data?.let { response ->
                            viewState.postValue(LoadPets(response))
                        }
                    }
                }
            } else {
                viewState.postValue(Error("Error Occurred.. Please logout then login again."))
            }
        }
    }

    var viewState = MutableLiveData<ViewState>()
    var actionState = SingleLiveEvent<ActionState>()

    sealed class ViewState() {
        object Loading : ViewState()
        data class LoadRecentAppointments(val appointments: List<Appointment>) : ViewState()
        data class LoadPets(val pets: List<Pet>) : ViewState()
        data class Error(val errorMessage: String) : ViewState()
    }

    sealed class ActionState() {

    }
}