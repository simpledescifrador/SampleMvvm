package com.jimac.vetclinicapp.ui.base

import androidx.lifecycle.ViewModel
import com.jimac.vetclinicapp.data.AppDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel(val appDataManager: AppDataManager) : ViewModel() {

    private var viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)
}