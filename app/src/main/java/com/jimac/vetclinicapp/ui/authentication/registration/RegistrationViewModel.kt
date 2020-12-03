package com.jimac.vetclinicapp.ui.authentication.registration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.data.models.Client
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ActionState.ProceedRegistration
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ActionState.SaveClientData
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.FormAgreementError
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.FormError
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.Loading
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.RegistrationFailure
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.RegistrationSuccess
import com.jimac.vetclinicapp.ui.base.BaseViewModel
import com.jimac.vetclinicapp.utils.AppUtils
import com.jimac.vetclinicapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class RegistrationViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RegistrationViewModel(mAppDataManager) as T
        }
    }

    fun validateForm(client: Client, agreement: Boolean) {
        var formValidation = true

        var fNameError: String? = null
        var lNameError: String? = null
        var addressError: String? = null
        var cNumberError: String? = null
        var emailError: String? = null
        var passwordError: String? = null

        //First Name Validations
        if (client.firstName.isEmpty()) {
            fNameError = "Required field"
            formValidation = false
        }

        //Last Name Validations
        if (client.lastName.isEmpty()) {
            lNameError = "Required field"
            formValidation = false
        }

        //Address Validations
        if (client.address.isEmpty()) {
            addressError = "Required field"
            formValidation = false
        }

        //Contact Number Validations
        if (client.contactNumber.isEmpty()) {
            cNumberError = "Required field"
            formValidation = false
        }

        //Email Validations
        if (client.email.isEmpty()) {
            emailError = "Required field"
            formValidation = false
        } else if (!AppUtils.isValidEmail(client.email)) {
            emailError = "Invalid email"
            formValidation = false
        }

        //Password Validation
        if (client.password.isEmpty()) {
            passwordError = "Required field"
            formValidation = false
        } else if (client.password.length < 3) {
            passwordError = "Password is too short"
            formValidation = false
        }

        //Set Form Error If have error
        viewState.value = FormError(fNameError, lNameError, addressError, cNumberError, emailError, passwordError)

        if (formValidation) {
            if (!agreement) {
                viewState.value = FormAgreementError("Please agree to our terms and condition")
            } else {
                actionState.value = ProceedRegistration(client)
            }
        }
    }

    fun register(client: Client) {
        Log.d("RegistrationViewModel", "Request Registration Viewmodel")
        viewState.value = Loading
        viewModelScope.launch {
            appDataManager.networkRepository.registerClient(client).run {
                if (status == 0) {
                    if (errorCode != 0) {
                        when (errorCode) {
                            99 -> {
                                //Internal Error 505;
                                Log.e("RegistrationViewModel", message)
                            }
                        }
                    }

                    viewState.postValue(RegistrationFailure(message))
                } else {
                    //Success Registration
                    viewState.postValue(RegistrationSuccess)
                    actionState.postValue(data?.let { SaveClientData(it) })
                }

            }
        }
    }

    fun saveClientData(client: Client) {
        Thread().run {
            appDataManager.sharedPreferenceManager.saveClientData(client)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDate = sdf.format(Date())

            appDataManager.sharedPreferenceManager.saveLoginDateTime(currentDate)
        }
    }

    val viewState = MutableLiveData<ViewState>()
    val actionState = SingleLiveEvent<ActionState>() // each value emit only once

    sealed class ViewState() {
        object Loading : ViewState()
        object RegistrationSuccess : ViewState()
        data class RegistrationFailure(val errorMessage: String) : ViewState()
        data class FormError(
            val fNameError: String?,
            val lNameError: String?,
            val addressError: String?,
            val cNumberError: String?,
            val emailError: String?,
            val passwordError: String?
        ) : ViewState()

        data class FormAgreementError(val errorMessage: String) : ViewState()
    }

    sealed class ActionState() {
        data class ProceedRegistration(var client: Client) : ActionState()
        data class SaveClientData(var client: Client) : ActionState()
    }
}