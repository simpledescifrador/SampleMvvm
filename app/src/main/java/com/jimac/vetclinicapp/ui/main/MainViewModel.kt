package com.jimac.vetclinicapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.ui.base.BaseViewModel

class MainViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(mAppDataManager) as T
        }
    }
}