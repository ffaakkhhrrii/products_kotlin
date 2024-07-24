package com.fakhri.products.data.network.user

import com.fakhri.products.data.network.response.user.Users
import com.fakhri.products.data.network.api.ProductService
import retrofit2.Response

class UserRemoteDataSourceImpl(private val apiService: ProductService) : UserRemoteDataSource {
    override suspend fun getUser(id: Int): Response<Users> {
        return apiService.getUsersId(id)
    }
}