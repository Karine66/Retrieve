package com.karine.retrieve.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karine.retrieve.repositories.DeleteAllObjectFromCurrentUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DeleteAllObjectCurrentUserViewModel(private val deleteAllObjectFromCurrentUserRepository: DeleteAllObjectFromCurrentUserRepository
) : ViewModel(), CoroutineScope {
    //set coroutine context
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob
    //coroutine job
    private var deleteAllObjectUserJob: Job? = null
    private var deleteAllObjectFindJob: Job?=null
    //live data
    private val _snackbarText = MutableLiveData<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarText

    /**
     * For delete all object find when user delete account
     */
    fun deleteAllObjectFindFromUser() {
        if (deleteAllObjectFindJob?.isActive == true) deleteAllObjectFindJob?.cancel()
        deleteAllObjectFindJob = launch {
            deleteAllObjectFromCurrentUserRepository.deleteAllUserObjectFindFromCurrentUser()
        }
    }

    /**
     * For delete all object lost when user delete account
     */
    fun deleteAllObjectLostFromUser() {
        if (deleteAllObjectUserJob?.isActive == true) deleteAllObjectUserJob?.cancel()
        deleteAllObjectUserJob = launch {
            deleteAllObjectFromCurrentUserRepository.deleteAllUserObjectLostFromCurrentUser()
        }
    }
}