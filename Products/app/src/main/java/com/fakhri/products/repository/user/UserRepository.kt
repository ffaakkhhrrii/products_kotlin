package com.fakhri.products.repository.user

import android.content.Context
import android.util.Log
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.user.UserLocalDataSourceImpl
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.network.user.UserRemoteDataSourceImpl
import com.fakhri.products.network.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val api: ProductService,
    private val context: Context
) : IUserRepository {

    private val dao = AppDatabase.getInstance(context).userDao()
    private val remoteDataSource = UserRemoteDataSourceImpl(api)
    private val localDataSource = UserLocalDataSourceImpl(dao)

    override fun getUser(id: Int): Flow<Result<Users>> {
        return flow {
            emit(Result.Loading)
            try {
                emit(Result.Success(getData(id)))
            } catch (e: Exception) {
                emit(Result.Failure(e))
            }
        }
    }

    override suspend fun addUser(users: Users) {
        localDataSource.addUser(users)
    }

    override suspend fun resetUser(id: Int) {
        localDataSource.deleteUser(id)
    }

    override suspend fun getUserFromDB(id: Int): Flow<Result<Users>> {
        return flow {
            emit(Result.Loading)
            try {
                emit(Result.Success(localDataSource.getUser(id)))
            } catch (e: Exception) {
                Log.i("DBProducts", e.message.toString())
                emit(Result.Failure(e))
            }
        }
    }


    private suspend fun getData(id: Int): Users {
        var user: Users? = null
        try {
            user = localDataSource.getUser(id)
        } catch (e: Exception) {
            Log.i("Products", e.message.toString())
        }
        if (user != null) {
            return user
        } else {
            user = getDataFromAPI(id)
            localDataSource.addUser(user)
        }
        return user
    }

    private suspend fun getDataFromAPI(id: Int): Users {
        var users: Users? = null
        try {
            val response = remoteDataSource.getUser(id)
            users = response.body()
        } catch (e: Exception) {
            Log.i("Products", e.message.toString())
        }
        return users!!
    }

}