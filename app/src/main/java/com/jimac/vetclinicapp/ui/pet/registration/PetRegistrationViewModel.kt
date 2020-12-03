package com.jimac.vetclinicapp.ui.pet.registration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.jimac.vetclinicapp.data.AppDataManager
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.ui.base.BaseViewModel
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ActionState.ProceedToPetRegistration
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.ChangeBreedList
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.FormError
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.InitLoading
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.Loading
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.OnLoadedPetData
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.PetRegistrationFailure
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.PetRegistrationSuccess
import com.jimac.vetclinicapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PetRegistrationViewModel(appDataManager: AppDataManager?) : BaseViewModel(appDataManager!!) {

    internal class ViewModelFactory(private val mAppDataManager: AppDataManager) : Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PetRegistrationViewModel(mAppDataManager) as T
        }
    }

    private var breedsMap = HashMap<String, ArrayList<String>>()
    private var species = ArrayList<String>()
    private var sizes = ArrayList<String>()

    fun initPetData() {
        viewState.value = InitLoading
        viewModelScope.launch {
            appDataManager.networkRepository.loadPetData().run {
                data?.let {
                    species = it.petSpecies
                    breedsMap = it.petBreeds
                    sizes = it.petSize
                }

                viewState.postValue(OnLoadedPetData(species, sizes))
            }
        }
    }

    fun loadBreeds(species: String) {
        viewState.value = ChangeBreedList(breedsMap[species])
    }

    fun browseImage() {
    }

    fun validateForm(pet: Pet) {
        var formValidation = true

        var petNameError: String? = null
        var speciesError: String? = null
        var breedError: String? = null
        var ageError: String? = null
        var sizeError: String? = null
        var heightError: String? = null
        var weightError: String? = null
        var descriptionError: String? = null

        //Pet name validations
        if (pet.name.isEmpty()) {
            petNameError = "Required field"
            formValidation = false
        }

        //Species validations
        if (pet.species.isEmpty()) {
            speciesError = "Required field"
            formValidation = false
        } else if (!species.contains(pet.species)) {
            speciesError = "Invalid species"
            formValidation = false
        }

        //Breed validations
        if (!pet.breed.isEmpty() && !pet.species.isEmpty()) {
            var isFound = false

            for ((key, value) in breedsMap) {
                if (key == pet.species) {
                    if (value.contains(pet.breed)) {
                        isFound = true
                    }
                }
            }

            if (!isFound) {
                breedError = "Invalid breed"
                formValidation = false
            }
        }

        //Age validations
        if (pet.age > 150 || pet.age < 0) {
            ageError = "Invalid age"
            formValidation = false
        }

        //Size Validations
        if (!pet.size.isEmpty() && !sizes.contains(pet.size)) {
            sizeError = "Invalid size"
            formValidation = false
        }

        //Set Form Error If have error
        viewState.value = FormError(
            petNameError,
            speciesError,
            breedError,
            ageError,
            sizeError,
            heightError,
            weightError,
            descriptionError
        )

        if (formValidation) {
            actionState.value = ProceedToPetRegistration(pet)
        }
    }

    fun submitPetRegistration(pet: Pet) {
        viewState.value = Loading

        //Get Client ID
        val clientId = appDataManager.sharedPreferenceManager.clientData.clientId
        if (clientId != null) {
            pet.clientId = clientId
        }

        viewModelScope.launch {
            appDataManager.networkRepository.registerPet(pet).run {
                if (status == 0) {
                    if (errorCode != 0) {
                        when (errorCode) {
                            99 -> {
                                //Internal Error 505;
                                Log.e("PetRegViewModel", message)
                            }
                        }
                    }

                    viewState.postValue(PetRegistrationFailure(message))
                } else {
                    //Success Pet Registration
                    viewState.postValue(PetRegistrationSuccess)
                }
            }
        }
    }

    val viewState = MutableLiveData<ViewState>()
    val actionState = SingleLiveEvent<ActionState>() // each value emit only once

    sealed class ViewState() {
        object Loading : ViewState()
        object InitLoading : ViewState()
        data class ChangeBreedList(var breeds: ArrayList<String>?) : ViewState()
        data class PetRegistrationFailure(var errorMessage: String) : ViewState()
        object PetRegistrationSuccess : ViewState()
        data class OnLoadedPetData(
            var species: ArrayList<String>,
            var size: ArrayList<String>
        ) : ViewState()

        data class FormError(
            var petNameError: String?,
            var speciesError: String?,
            var breedError: String?,
            var ageError: String?,
            var sizeError: String?,
            var heightError: String?,
            var weightError: String?,
            var descriptionError: String?,
        ) : ViewState()
    }

    sealed class ActionState() {
        data class ProceedToPetRegistration(var pet: Pet) : ActionState()
    }
}