package com.karine.retrieve.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.UserObjectFirebaseRepository
import java.util.*


class UserObjectViewModel : ViewModel() {

    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = UserObjectFirebaseRepository()
    var savedUserObjectFind: MutableLiveData<List<UserObject>> = MutableLiveData()
    var savedUserObjectLost: MutableLiveData<List<UserObject>> = MutableLiveData()
    var firestoreDB: FirebaseFirestore = FirebaseFirestore.getInstance()

//    save user object find to firebase
//    fun saveUserObjectFindToFirebase(userObject: UserObject) {
//        firebaseRepository.saveUserObjectFind(userObject).addOnSuccessListener {
//            Log.d("addObjectFind", "DocumentSnapshot successfully written!")
//        }.addOnFailureListener {
//            Log.e(TAG, "Failed to save UserObjectFind")
//        }
//    }
//
//    //save user object lost to firebase
//    fun saveUserObjectLostToFirebase(userObject: UserObject) {
//        firebaseRepository.saveUserObjectLost(userObject).addOnSuccessListener {
//            Log.d("addObjectLost", "DocumentSnapshot successfully written!")
//        }.addOnFailureListener {
//            Log.e(TAG, "Failed to save UserObjectLost")
//        }
//    }

    //get realtime updates for firebase regarding saved user object find
    fun getSavedUserObjectFind(): LiveData<List<UserObject>> {
        firebaseRepository.getSavedUserObjectFind()
            .addSnapshotListener(com.google.firebase.firestore.EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed", e)
                    savedUserObjectFind.value = null
                    return@EventListener
                }

                val savedUserObjectFindList: MutableList<UserObject> = mutableListOf()
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
            .addSnapshotListener(com.google.firebase.firestore.EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed", e)
                    savedUserObjectLost.value = null
                    return@EventListener
                }

                val savedUserObjectLostList: MutableList<UserObject> = mutableListOf()
                for (doc in value!!) {
                    var userObject = doc.toObject(UserObject::class.java)
                    savedUserObjectLostList.add(userObject)
                }
                savedUserObjectLost.value = savedUserObjectLostList
            })
        return savedUserObjectLost
    }

    //delete an user object find from firebase
    fun deleteObjectFind(userObject: UserObject) {
        firebaseRepository.deleteUserObjectFind(userObject).addOnSuccessListener {
            Log.d("deleteObjectFind", "DocumentSnapshot successfully delete!")
        }.addOnFailureListener {
            Log.e(TAG, "Failed to delete User Object Find")
        }
    }

    //delete an user object lost from firebase
    fun deleteObjectLost(userObject: UserObject) {
        firebaseRepository.deleteUserObjectLost(userObject).addOnSuccessListener {
            Log.d("deleteObjectLost", "DocumentSnapshot successfully delete !")
        }.addOnFailureListener {
            Log.e(TAG, "Failed to delete User Object Lost")
        }
    }

    //for delete announcement when user delete account
    fun deleteAllUserObjectFindFromCurrentUser() {
        firebaseRepository.getSavedUserObjectFind()
            .whereEqualTo("uid", FirebaseAuth.getInstance().uid)
            .get()
            .addOnSuccessListener {
//                var batch = firestoreDB.batch()
                firestoreDB.runBatch { batch ->
                    for (doc in it.documents) {
                        val userObject = doc.toObject(UserObject::class.java)
                        userObject?.let {
                            var collectionRefFind =
                                firestoreDB.collection("usersObjectFind").document(userObject.docId)
                            batch.delete(collectionRefFind)
                        }
                    }
                }.addOnSuccessListener(OnSuccessListener<Void?> {
                    Log.d("deleteUserwithobject", "deleteUserwithObject")
                })
            }
    }

    fun deleteAllUserObjectLostFromCurrentUser() {
        firebaseRepository.getSavedUserObjectLost()
            .whereEqualTo("uid", FirebaseAuth.getInstance().uid)
            .get()
            .addOnSuccessListener {
                firestoreDB.runBatch { batch ->
                    for (doc in it.documents) {
                        val userObject = doc.toObject(UserObject::class.java)
                        userObject?.let {
                            var collectionRefLost =
                                firestoreDB.collection("usersObjectLost").document(userObject.docId)
                            batch.delete(collectionRefLost)
                        }
                        }
                    } .addOnSuccessListener(OnSuccessListener<Void?> {
                    Log.d("deleteUserwithobject", "deleteUserwithObject")
                })
            }
    }
}