package com.karine.retrieve.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.models.UserObject

class UserObjectFirebaseRepository {

    val TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    //save user object to firebase
   fun saveUserObject(userObject: UserObject) : Task<Void> {
       var documentReference = firestoreDB.collection("users").document(user!!.email.toString())
           .collection("saved_UserObject").document(userObject.uid)
        return documentReference.set(userObject)
   }
    //get saved user object from firebase
    fun getSavedUserObject() : CollectionReference {
        var collectionReference = firestoreDB.collection("users/${user!!.email.toString()}/saved_UserObject")
        return collectionReference
    }
    //delete user object from firebase
    fun deleteUserObject(userObject: UserObject) : Task<Void> {
        var documentReference = firestoreDB.collection("users/${user!!.email.toString()}/saved_UserObject")
            .document(userObject.uid)
        return documentReference.delete()
    }

}