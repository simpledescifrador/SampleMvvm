package com.jimac.vetclinicapp.ui.main.fragments.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.ui.base.BaseViewModel

class AppointmentViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AppointmentViewModel(mAppDataManager) as T
        }
    }
}