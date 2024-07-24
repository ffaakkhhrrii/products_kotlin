package com.fakhri.products.data.network.user

import com.fakhri.products.data.network.response.user.Users
import retrofit2.Response

interface UserRemoteDataSource {
    suspend fun getUser(id:Int): Response<Users>
}