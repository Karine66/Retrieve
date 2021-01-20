package com.karine.retrieve

import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.SaveUserObjectRepository
import com.karine.retrieve.utils.Result

open class SaveUserObjectRepositoryImplTest : SaveUserObjectRepository {
    override suspend fun saveUserObjectFindInFirestore(userObject: UserObject): Result<Void?>
    = Result.Success(null)

    override suspend fun saveUserObjectLostInFirestore(userObject: UserObject): Result<Void?>
    = Result.Success(null)

}