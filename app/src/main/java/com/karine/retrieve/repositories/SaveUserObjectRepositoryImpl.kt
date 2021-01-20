package com.karine.retrieve.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.utils.Result
import com.karine.retrieve.utils.TaskExt
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SaveUserObjectRepositoryimpl : SaveUserObjectRepository, TaskExt() {

    var firestoreDB = FirebaseFirestore.getInstance()
    var collectionRefFind = firestoreDB.collection("usersObjectFind")
    var collectionRefLost = firestoreDB.collection("usersObjectLost")


    override suspend fun saveUserObjectFindInFirestore(userObject: UserObject): Result<Void?> {
        val documentId : String = collectionRefFind.document().id
        userObject.docId = documentId
     return collectionRefFind.document(documentId).set(userObject).await()
    }

    override suspend fun saveUserObjectLostInFirestore(userObject: UserObject): Result<Void?> {
        val documentId : String = collectionRefLost.document().id
        userObject.docId = documentId
        return collectionRefLost.document(documentId).set(userObject).await()
    }
}