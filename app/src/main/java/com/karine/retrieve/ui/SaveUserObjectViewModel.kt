package com.karine.retrieve.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.karine.retrieve.R
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.SaveUserObjectRepository
import com.karine.retrieve.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SaveUserObjectViewModel (
    private val saveUserObjectRepository: SaveUserObjectRepository
) : ViewModel(),
    CoroutineScope {
    //set coroutine context
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    //coroutine job
    private var saveUserObjectJob : Job? = null
    //live data
    private val _snackbarText = MutableLiveData<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarText

//    fun saveUserObjectFindToFirestore(
//        docId: String, uid: String, created: Timestamp?, pseudo: String?,
//        email:String?, phone:String?, date:String?, type:String?, address:String?, postalCode:Int?,
//        city:String?, description:String?, photo:MutableList<String>) {
//        val objectUser = UserObject(docId = docId, uid = uid, created = com.google.firebase.Timestamp.now(),pseudo = pseudo, email = email,
//        phone = phone, date = date, type = type, address = address, postalCode = postalCode, city=city,
//        description = description, photo = photo)
//        if(saveUserObjectJob?.isActive == true) saveUserObjectJob?.cancel()
//        saveUserObjectJob = launch {
//            when (saveUserObjectRepository.saveUserObjectFindInFirestore(objectUser)) {
//                is Result.Success->_snackbarText.value = R.string.createdUO
//                is Result.Error->_snackbarText.value = R.string.errInconnue
//                is Result.Canceled->_snackbarText.value = R.string.cancel
//            }
//        }
//    }

    fun saveUserObjectFindToFirestore( userObject: UserObject){
        if(saveUserObjectJob?.isActive == true) saveUserObjectJob?.cancel()
        saveUserObjectJob = launch {
            when (saveUserObjectRepository.saveUserObjectFindInFirestore(userObject)) {
                is Result.Success->_snackbarText.value = R.string.createdUO
                is Result.Error->_snackbarText.value = R.string.errInconnue
                is Result.Canceled->_snackbarText.value = R.string.cancel
            }
        }
    }
    fun saveUserObjectLostToFirestore(docId : String, uid : String, created: com.google.firebase.Timestamp?, pseudo : String?,
                                      email:String?, phone:String?, date:String?, type:String?, address:String?, postalCode:Int?,
                                      city:String?, description:String?, photo:MutableList<String>) {
        val objectUser = UserObject(docId = docId, uid = uid, created = com.google.firebase.Timestamp.now(),pseudo = pseudo, email = email,
            phone = phone, date = date, type = type, address = address, postalCode = postalCode, city=city,
            description = description, photo = photo)
        if(saveUserObjectJob?.isActive == true) saveUserObjectJob?.cancel()
        saveUserObjectJob = launch {
            when (saveUserObjectRepository.saveUserObjectLostInFirestore(objectUser)) {
                is Result.Success->_snackbarText.value = R.string.createdUO
                is Result.Error->_snackbarText.value = R.string.errInconnue
                is Result.Canceled->_snackbarText.value = R.string.cancel
            }
        }
    }

}