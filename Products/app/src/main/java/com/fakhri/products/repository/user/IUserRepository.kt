package com.fakhri.products.repository.user

import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUser(id: Int): Flow<Result<Users>>
    suspend fun addUser(users: Users)
    suspend fun resetUser(id: Int)
    suspend fun getUserFromDB(id: Int): Flow<Result<Users>>
}