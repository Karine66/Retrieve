package com.karine.retrieve.repositories

import com.google.firebase.firestore.CollectionReference

interface DeleteAllObjectFromCurrentUserRepository {

    suspend fun getSavedUserObjectFind() : CollectionReference
    suspend fun getSavedUserObjectLost(): CollectionReference

    suspend fun deleteAllUserObjectFindFromCurrentUser()
    suspend fun deleteAllUserObjectLostFromCurrentUser()
}