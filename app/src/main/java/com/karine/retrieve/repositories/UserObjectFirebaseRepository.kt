package com.karine.retrieve.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.models.UserObject

class UserObjectFirebaseRepository {

    val TAG = "FIREBASE_REPOSITORY"

    var firestoreDB = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    var collectionRefFind = firestoreDB.collection("usersObjectFind")
    var collectionRefLost = firestoreDB.collection("usersObjectLost")

////    save user object find to firebase
//   fun saveUserObjectFind(userObject: UserObject) : Task<Void> {
//       val documentId : String = collectionRefFind.document().id
//        userObject.docId = documentId
//        return collectionRefFind.document(documentId).set(userObject)
//   }
//
//    //save user object lost to firebase
//    fun saveUserObjectLost(userObject: UserObject) : Task<Void> {
//        val documentId : String = collectionRefLost.document().id
//        userObject.docId = documentId
//        return collectionRefLost.document(documentId).set(userObject)
//    }



    //get saved user object find from firebase
    fun getSavedUserObjectFind() : CollectionReference {
        val collectionReference = firestoreDB.collection("usersObjectFind")
        return collectionReference
    }

    //get saved user object lost from firebase
    fun getSavedUserObjectLost() : CollectionReference {
        val collectionReference = firestoreDB.collection("usersObjectLost")
        return collectionReference
    }
    //delete user object find  from firebase
    fun deleteUserObjectFind(userObject: UserObject) : Task<Void> {
        val documentReference = firestoreDB.collection("usersObjectFind")
           .document(userObject.docId)
        return documentReference.delete()
    }
    //delete user object Lost from firebase
    fun deleteUserObjectLost(userObject: UserObject) : Task<Void> {
        val documentReference = firestoreDB.collection("usersObjectLost")
            .document(userObject.docId)
        return documentReference.delete()
    }

}