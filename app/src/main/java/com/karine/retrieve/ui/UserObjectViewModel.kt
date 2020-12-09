package com.karine.retrieve.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.UserObjectFirebaseRepository
import java.util.*


class UserObjectViewModel : ViewModel() {

    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = UserObjectFirebaseRepository()
    var savedUserObject: MutableLiveData<List<UserObject>> = MutableLiveData()

    //save user object to firebase
    fun saveUserObjectToFirebase(userObject: UserObject) {
        firebaseRepository.saveUserObject(userObject).addOnFailureListener {
            Log.e(TAG, "Failed to save UserObject")
        }
    }

    //get realtime updates for firebase regarding saved user object
    fun getSavedUserObject(): LiveData<List<UserObject>> {
        firebaseRepository.getSavedUserObject()
            .addSnapshotListener(com.google.firebase.firestore.EventListener <QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed", e)
                    savedUserObject.value = null
                    return@EventListener
                }

                var savedUserObjectList: MutableList<UserObject> = mutableListOf()
                for (doc in value!!) {
                    var userObject = doc.toObject(UserObject::class.java)
                    savedUserObjectList.add(userObject)
                }
                savedUserObject.value = savedUserObjectList
            })
        return savedUserObject
    }

    //delete an user object from firebase
    fun deleteUserObject(userObject: UserObject) {
        firebaseRepository.deleteUserObject(userObject).addOnFailureListener{
            Log.e(TAG, "Failed to delete User Object")
        }
    }

}