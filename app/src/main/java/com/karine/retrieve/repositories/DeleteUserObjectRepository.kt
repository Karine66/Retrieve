package com.karine.retrieve.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.utils.Result

interface DeleteUserObjectRepository {

    suspend fun deleteUserObjectFindInFirestore(userObject: UserObject) : Result<Void?>
    suspend fun deleteUserObjectLostInFirestore(userObject: UserObject) : Result<Void?>
}