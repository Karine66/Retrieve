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

    /**
     * For create object find in firebase
     */
    override suspend fun saveUserObjectFindInFirestore(userObject: UserObject): Result<Void?> {
        val documentId: String = collectionRefFind.document().id
        userObject.docId = documentId
        return collectionRefFind.document(documentId).set(userObject).await()
    }

    /**
     * For create object lost in firebase
     */
    override suspend fun saveUserObjectLostInFirestore(userObject: UserObject): Result<Void?> {
        val documentId: String = collectionRefLost.document().id
        userObject.docId = documentId
        return collectionRefLost.document(documentId).set(userObject).await()
    }
}