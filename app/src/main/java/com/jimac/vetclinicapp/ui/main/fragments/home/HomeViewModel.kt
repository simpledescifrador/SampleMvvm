package com.jimac.vetclinicapp.ui.main.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.ui.base.BaseViewModel
import com.jimac.vetclinicapp.ui.main.fragments.home.HomeViewModel.ViewState.Loading
import com.jimac.vetclinicapp.utils.SingleLiveEvent

class HomeViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {
    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(mAppDataManager) as T
        }
    }

    fun test() {
        viewState.value = Loading
    }

    var viewState = MutableLiveData<ViewState>()
    var actionState = SingleLiveEvent<ActionState>()

    sealed class ViewState() {
        object Loading : ViewState()
    }

    sealed class ActionState() {

    }
}