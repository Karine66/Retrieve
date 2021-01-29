package com.karine.retrieve

import com.karine.retrieve.models.UserObject
import com.karine.retrieve.repositories.DeleteUserObjectRepository
import com.karine.retrieve.utils.Result

class DeleteUserObjectRepositoryImplTest : DeleteUserObjectRepository {
    override suspend fun deleteUserObjectFindInFirestore(userObject: UserObject): Result<Void?>
        = Result.Success(null)

    override suspend fun deleteUserObjectLostInFirestore(userObject: UserObject): Result<Void?>
       = Result.Success(null)
}