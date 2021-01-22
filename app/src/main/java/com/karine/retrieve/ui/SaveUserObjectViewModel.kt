package com.karine.retrieve.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karine.retrieve.R
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.SaveUserObjectRepository
import com.karine.retrieve.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SaveUserObjectViewModel(
    private val saveUserObjectRepository: SaveUserObjectRepository
) : ViewModel(),
    CoroutineScope {
    //set coroutine context
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    //coroutine job
    private var saveUserObjectJob: Job? = null

    //live data
    private val _snackbarText = MutableLiveData<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarText

    /**
     * For create object find in firestore
     */
    fun saveUserObjectFindToFirestore(userObject: UserObject) {
        if (saveUserObjectJob?.isActive == true) saveUserObjectJob?.cancel()
        saveUserObjectJob = launch {
            when (saveUserObjectRepository.saveUserObjectFindInFirestore(userObject)) {
                is Result.Success -> _snackbarText.value = R.string.createdUO
                is Result.Error -> _snackbarText.value = R.string.errInconnue
                is Result.Canceled -> _snackbarText.value = R.string.cancel
            }
        }
    }

    /**
     * For create object lost in firebase
     */
    fun saveUserObjectLostToFirestore(userObject: UserObject) {
        if (saveUserObjectJob?.isActive == true) saveUserObjectJob?.cancel()
        saveUserObjectJob = launch {
            when (saveUserObjectRepository.saveUserObjectLostInFirestore(userObject)) {
                is Result.Success -> _snackbarText.value = R.string.createdUO
                is Result.Error -> _snackbarText.value = R.string.errInconnue
                is Result.Canceled -> _snackbarText.value = R.string.cancel
            }
        }
    }
}