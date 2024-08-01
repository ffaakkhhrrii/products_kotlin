package com.fakhri.products.domain

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.network.response.user.Users
import com.fakhri.products.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUser(id: Int): Flow<Resource<UsersEntity>>
    suspend fun addUser(users: UsersEntity)
    suspend fun resetUser(id: Int)
    fun getUserFromDB(id: Int): Flow<Resource<UsersEntity>>
}