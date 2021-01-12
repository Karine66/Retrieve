package com.karine.retrieve.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.models.UserObject

class UserObjectFirebaseRepository {

    val TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    //save user object find to firebase
   fun saveUserObjectFind(userObject: UserObject) : Task<DocumentReference> {
       var documentReference = firestoreDB.collection("usersObjectFind").add(userObject)
//           .collection("saved_UserObject").document(userObject.uid)
        return documentReference
   }

    //save user object lost to firebase
    fun saveUserObjectLost(userObject: UserObject) : Task<DocumentReference> {
        var documentReference = firestoreDB.collection("usersObjectLost").add(userObject)
//           .collection("saved_UserObject").document(userObject.uid)
        return documentReference
    }
    //get saved user object find from firebase
    fun getSavedUserObjectFind() : CollectionReference {
//        var collectionReference = firestoreDB.collection("users/${user!!.email.toString()}/saved_UserObject")
        var collectionReference = firestoreDB.collection("usersObjectFind/${user!!.email.toString()}")
        return collectionReference
    }

    //get saved user object lost from firebase
    fun getSavedUserObjectLost() : CollectionReference {
//        var collectionReference = firestoreDB.collection("users/${user!!.email.toString()}/saved_UserObject")
        var collectionReference = firestoreDB.collection("usersObjectLost/${user!!.email.toString()}")
        return collectionReference
    }
    //delete user object find  from firebase
    fun deleteUserObjectFind(userObject: UserObject) : Task<Void> {
        var documentReference = firestoreDB.collection("usersObjectFind")
           .document(userObject.uid)
        return documentReference.delete()
    }
    //delete user object Lost from firebase
    fun deleteUserObjectLost(userObject: UserObject) : Task<Void> {
        var documentReference = firestoreDB.collection("usersObjectLost")
            .document(userObject.uid)
        return documentReference.delete()
    }

}