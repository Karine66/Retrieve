package com.karine.retrieve.repositories

import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.utils.Result
import com.karine.retrieve.utils.TaskExt

class DeleteUserObjetRepositoryImpl : DeleteUserObjectRepository, TaskExt() {

    var firestoreDB = FirebaseFirestore.getInstance()

    /**
     * For delete object find find
     */
    override suspend fun deleteUserObjectFindInFirestore(userObject: UserObject): Result<Void?> {
        val documentReference = firestoreDB.collection("usersObjectFind")
            .document(userObject.docId).delete()
        return documentReference.await()
    }

    /**
     * For delete object lost
     */
    override suspend fun deleteUserObjectLostInFirestore(userObject: UserObject): Result<Void?> {
        val documentReference = firestoreDB.collection("usersObjectLost")
            .document(userObject.docId).delete()
        return documentReference.await()
    }

    override suspend fun getSavedUserObjectFind(): CollectionReference {
        val collectionReference = firestoreDB.collection("usersObjectFind")
        return collectionReference
    }

    override suspend fun getSavedUserObjectLost(): CollectionReference {
        val collectionReference = firestoreDB.collection("usersObjectLost")
        return collectionReference
    }

    /**
     * For delete object find when user delete account
     */
    override suspend fun deleteAllUserObjectFindFromCurrentUser() {
        getSavedUserObjectFind()
            .whereEqualTo("uid", FirebaseAuth.getInstance().uid)
            .get()
            .addOnSuccessListener {
                FirebaseFirestore.getInstance().runBatch { batch ->
                    for (doc in it.documents) {
                        val userObject = doc.toObject(UserObject::class.java)
                        userObject?.let {
                            var collectionRefFind =
                                FirebaseFirestore.getInstance().collection("usersObjectFind")
                                    .document(userObject.docId)
                            batch.delete(collectionRefFind)
                        }
                    }
                }.addOnSuccessListener(OnSuccessListener<Void?> {
                    Log.d("deleteUserwithobject", "deleteUserwithObject")
                })
            }
    }

    /**
     * For delete object lost when user delete account
     */
    override suspend fun deleteAllUserObjectLostFromCurrentUser() {
        getSavedUserObjectLost()
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
                }.addOnSuccessListener(OnSuccessListener<Void?> {
                    Log.d("deleteUserwithobject", "deleteUserwithObject")
                })
            }
    }
}