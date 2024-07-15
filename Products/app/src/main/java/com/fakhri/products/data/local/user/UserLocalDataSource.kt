package com.fakhri.products.data.local.user

import com.fakhri.products.data.network.model.user.Users

interface UserLocalDataSource {
    fun getUser(id: Int): Users
    suspend fun addUser(users: Users)
    suspend fun deleteUser(id: Int)
}