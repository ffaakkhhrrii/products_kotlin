package com.fakhri.products.data.local.user

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.network.response.user.Users

interface UserLocalDataSource {
    fun getUser(id: Int): UsersEntity
    suspend fun addUser(users: UsersEntity)
    suspend fun deleteUser(id: Int)
}