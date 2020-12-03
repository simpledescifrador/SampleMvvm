package com.jimac.vetclinicapp.ui.authentication.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.data.models.Client
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ActionState.ProceedLogin
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ActionState.SaveClientData
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ActionState.SkipLogin
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.FormError
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.Loading
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.LoginFailure
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.LoginSuccess
import com.jimac.vetclinicapp.ui.base.BaseViewModel
import com.jimac.vetclinicapp.utils.AppUtils
import com.jimac.vetclinicapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class LoginViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(mAppDataManager) as T
        }
    }

    val viewState = MutableLiveData<ViewState>()
    val actionState = SingleLiveEvent<ActionState>() // each value emit only once

    fun validateForm(emailAddress: String, password: String) {
        var formValidation = true

        var emailError: String? = null
        var passwordError: String? = null

        //Email Address validations
        if (emailAddress.isEmpty()) {
            emailError = "Required field"
            formValidation = false
        } else if (!AppUtils.isValidEmail(emailAddress)) {
            emailError = "Invalid email"
            formValidation = false
        }

        //Password validations
        if (password.isEmpty()) {
            passwordError = "Required field"
            formValidation = false
        } else if (password.length < 3) {
            passwordError = "Password is too short"
            formValidation = false
        }

        //Set Form Error If have error
        viewState.value = FormError(emailError, passwordError)

        if (formValidation) {
            actionState.postValue(ProceedLogin(emailAddress, password))
        }
    }

    fun login(emailAddress: String, password: String) {
        viewState.value = Loading
        viewModelScope.launch {
            appDataManager.networkRepository.login(emailAddress, password).run {
                if (status == 0) {
                    if (errorCode != 0) {
                        when (errorCode) {
                            99 -> {
                                //Internal Error 505;
                                Log.e("LoginViewModel", message)
                            }
                        }
                    }

                    viewState.postValue(LoginFailure(message))
                } else {
                    //Success Registration
                    viewState.postValue(LoginSuccess)
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

    fun checkIfAlreadyLogin() {
        viewState.postValue(Loading)
        Thread().run {
            //TODO: Check Login Time HERE Validate if Expired 2 Hours Expiration Duration
            val loginDateTime = appDataManager.sharedPreferenceManager.loginDateTime
            val client = appDataManager.sharedPreferenceManager.clientData
            actionState.postValue(SkipLogin)
        }
    }

    sealed class ViewState() {
        object Loading : ViewState()
        object LoginSuccess : ViewState()
        data class LoginFailure(var errorMessage: String) : ViewState()
        data class FormError(
            val emailError: String?,
            val passwordError: String?
        ) : ViewState()
    }

    sealed class ActionState() {
        data class ProceedLogin(var emailAddress: String, var password: String) : ActionState()
        data class SaveClientData(var client: Client) : ActionState()
        object SkipLogin : ActionState()
    }
}