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

//    private val COLLECTION_NAME = "users"

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

//    fun getUsersCollection(): CollectionReference {
//        return FirebaseFirestore.getInstance()
//            .collection(COLLECTION_NAME)
//    }
//
//    fun createUser(
//        uid: String?,
//        pseudo: String?,
//        email: String?,
//        phone: Int?,
//        date: Long?,
//        type: String?,
//        address: String?,
//        postalCode: Int?,
//        city: String?,
//        description: String?
//    ): Task<Void?> {
//        //Create user object
//        val userToCreate = uid?.let {
//            UserObject(
//                it,
//                pseudo,
//                email,
//                phone,
//                date,
//                type,
//                address,
//                postalCode,
//                city,
//                description
//            )
//        }
//        //Add a new user Document in Firestore
//        return uid?.let {
//            getUsersCollection()
//                .document(it) //Setting uID for Document
//                .set(userToCreate!!)
//        }!! //Setting object for Document
//    }
//
//    fun getUser(uid: String?): Task<DocumentSnapshot?>? {
//        return uid?.let { getUsersCollection()?.document(it)?.get() }
//    }
//
//    fun updatePseudo(pseudo: String?, uid: String?): Task<Void?> {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("pseudo", pseudo)
//        } as Task<Void?>
//    }
//
//    fun updateEmail(uid: String?, email: String?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("email", email)
//        }
//    }
//
//    fun updatePhone(uid: String?, phone: Int?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("phone", phone)
//        }
//    }
//
//    fun updateDate(uid: String?, date: Long?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("date", date)
//        }
//    }
//
//    fun updateType(uid: String?, type: Int?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("type", type)
//        }
//    }
//
//    fun updateAddress(uid: String?, address: Long?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("address", address)
//        }
//    }
//    fun updatePostalCode(uid: String?, postalCode: Int?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("postalCode", postalCode)
//        }
//    }
//    fun updateCity(uid: String?, city: String?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("city", city)
//        }
//    }
//    fun updateDescription(uid: String?, description: String?): Task<Void?>? {
//        return uid?.let {
//            getUsersCollection()?.document(it)
//                .update("description", description)
//        }
//    }
//
//    fun deleteUser(uid: String?): Task<Void?> {
//        return uid?.let { getUsersCollection().document(it).delete() } as Task<Void?>
//    }
//
////    fun deletePseudo(uid: String?, pseudo: String?): Task<Void?>? {
////        return com.karine.retrieve.ui.UserObjectViewModel.getUsersCollection().document(uid)
////            .update("pseudo", null )
////    }
////
////    fun deleteLike(uid: String?, placeId: String?): Task<Void?>? {
////        return com.karine.go4lunch.API.UserHelper.getUsersCollection().document(uid)
////            .update("like", FieldValue.arrayRemove(placeId))
////    }
}