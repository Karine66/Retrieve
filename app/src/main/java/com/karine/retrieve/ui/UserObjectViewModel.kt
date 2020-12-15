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
    var savedUserObjectFind: MutableLiveData<List<UserObject>> = MutableLiveData()
    var savedUserObjectLost: MutableLiveData<List<UserObject>> = MutableLiveData()

    //save user object find to firebase
    fun saveUserObjectFindToFirebase(userObject: UserObject) {
        firebaseRepository.saveUserObjectFind(userObject).addOnSuccessListener {
            Log.d("addObjectFind", "DocumentSnapshot successfully written!")
        }.addOnFailureListener {
            Log.e(TAG, "Failed to save UserObjectFind")
        }
    }

    //save user object lost to firebase
    fun saveUserObjectLostToFirebase(userObject: UserObject) {
        firebaseRepository.saveUserObjectLost(userObject).addOnSuccessListener {
            Log.d("addObjectLost", "DocumentSnapshot successfully written!")
        }.addOnFailureListener {
            Log.e(TAG, "Failed to save UserObjectLost")
        }
    }

    //get realtime updates for firebase regarding saved user object find
    fun getSavedUserObjectFind(): LiveData<List<UserObject>> {
        firebaseRepository.getSavedUserObjectFind()
            .addSnapshotListener(com.google.firebase.firestore.EventListener <QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed", e)
                    savedUserObjectFind.value = null
                    return@EventListener
                }

                var savedUserObjectFindList: MutableList<UserObject> = mutableListOf()
                for (doc in value!!) {
                    var userObject = doc.toObject(UserObject::class.java)
                    savedUserObjectFindList.add(userObject)
                }
                savedUserObjectFind.value = savedUserObjectFindList
            })
        return savedUserObjectFind
    }
    //get realtime updates for firebase regarding saved user object find
    fun getSavedUserObjectLost(): LiveData<List<UserObject>> {
        firebaseRepository.getSavedUserObjectLost()
            .addSnapshotListener(com.google.firebase.firestore.EventListener <QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed", e)
                    savedUserObjectLost.value = null
                    return@EventListener
                }

                var savedUserObjectLostList: MutableList<UserObject> = mutableListOf()
                for (doc in value!!) {
                    var userObject = doc.toObject(UserObject::class.java)
                    savedUserObjectLostList.add(userObject)
                }
                savedUserObjectLost.value = savedUserObjectLostList
            })
        return savedUserObjectLost
    }

    //delete an user object find from firebase
    fun deleteUserObjectFind(userObject: UserObject) {
        firebaseRepository.deleteUserObjectFind(userObject).addOnFailureListener{
            Log.e(TAG, "Failed to delete User Object Find")
        }
    }
    //delete an user object lost from firebase
    fun deleteUserObjectLost(userObject: UserObject) {
        firebaseRepository.deleteUserObjectLost(userObject).addOnFailureListener{
            Log.e(TAG, "Failed to delete User Object Find")
        }
    }
}