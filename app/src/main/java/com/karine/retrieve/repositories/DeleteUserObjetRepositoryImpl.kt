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


}