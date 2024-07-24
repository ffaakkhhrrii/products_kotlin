package com.fakhri.products.data.local.user

import com.fakhri.products.data.local.db.user.UserDAO
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.network.response.user.Users

class UserLocalDataSourceImpl(private val userDao: UserDAO) :UserLocalDataSource {

    override fun getUser(id: Int): UsersEntity {
        return userDao.getUser(id)
    }

    override suspend fun addUser(users: UsersEntity) {
        userDao.insertUser(users)
    }

    override suspend fun deleteUser(id: Int) {
        userDao.deleteUser(id)
    }
}