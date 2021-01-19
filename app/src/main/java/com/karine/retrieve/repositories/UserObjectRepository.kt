package com.karine.retrieve.repositories

import com.karine.retrieve.models.UserObject
import com.karine.retrieve.utils.Result


interface UserObjectRepository {

    suspend fun saveUserObjectFindInFirestore(userObject: UserObject) : Result<Void?>
    suspend fun saveUserObjectLostInFirestore(userObject: UserObject) : Result<Void?>
}