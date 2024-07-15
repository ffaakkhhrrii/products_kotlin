package com.fakhri.products.data.local.user

import com.fakhri.products.data.local.db.user.UserDAO
import com.fakhri.products.data.network.model.user.Users

class UserLocalDataSourceImpl(private val userDao: UserDAO) :UserLocalDataSource {

    override fun getUser(id: Int): Users {
        return userDao.getUser(id)
    }

    override suspend fun addUser(users: Users) {
        userDao.insertUser(users)
    }

    override suspend fun deleteUser(id: Int) {
        userDao.deleteUser(id)
    }
}