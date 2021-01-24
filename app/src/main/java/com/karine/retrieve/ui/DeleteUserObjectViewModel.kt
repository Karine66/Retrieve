package com.karine.retrieve.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karine.retrieve.R
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.DeleteUserObjectRepository
import com.karine.retrieve.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DeleteUserObjectViewModel(
    private val deleteUserObjectRepository: DeleteUserObjectRepository
) : ViewModel(), CoroutineScope {
    //set coroutine context
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    //coroutine job
    private var deleteUserObjectJob: Job? = null
    private var deleteAllObjectUserJob: Job? = null
    private var deleteAllObjectFindJob: Job?=null

    //live data
    private val _snackbarText = MutableLiveData<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarText

    /**
     * For delete object find
     */
    fun deleteUserObjectFindToFirestore(userObject: UserObject) {
        if (deleteUserObjectJob?.isActive == true) deleteUserObjectJob?.cancel()
        deleteUserObjectJob = launch {
            when (deleteUserObjectRepository.deleteUserObjectFindInFirestore(userObject)) {
                is Result.Success -> _snackbarText.value = R.string.createdUO
                is Result.Error -> _snackbarText.value = R.string.errInconnue
                is Result.Canceled -> _snackbarText.value = R.string.cancel
            }
        }
    }

    /**
     * For delete object lost
     */
    fun deleteUserObjectLostToFirestore(userObject: UserObject) {
        if (deleteUserObjectJob?.isActive == true) deleteUserObjectJob?.cancel()
        deleteUserObjectJob = launch {
            when (deleteUserObjectRepository.deleteUserObjectLostInFirestore(userObject)) {
                is Result.Success -> _snackbarText.value = R.string.createdUO
                is Result.Error -> _snackbarText.value = R.string.errInconnue
                is Result.Canceled -> _snackbarText.value = R.string.cancel
            }
        }
    }

    /**
     * For delete all object find when user delete account
     */
    fun deleteAllObjectFindFromUser() {
        if (deleteAllObjectFindJob?.isActive == true) deleteAllObjectFindJob?.cancel()
        deleteAllObjectFindJob = launch {
            when (deleteUserObjectRepository.deleteAllUserObjectFindFromCurrentUser()) {
//                is Result.Success->_snackbarText.value = R.string.createdUO
//                is Result.Error->_snackbarText.value = R.string.errInconnue
//                is Result.Canceled->_snackbarText.value = R.string.cancel
            }
        }
    }

    /**
     * For delete all object lost when user delete account
     */
    fun deleteAllObjectLostFromUser() {
        if (deleteAllObjectUserJob?.isActive == true) deleteAllObjectUserJob?.cancel()
        deleteAllObjectUserJob = launch {
            when (deleteUserObjectRepository.deleteAllUserObjectLostFromCurrentUser()) {

            }
        }
    }
}