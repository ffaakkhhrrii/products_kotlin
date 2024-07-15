package com.fakhri.products.data.network.user

import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.network.ProductService
import retrofit2.Response

class UserRemoteDataSourceImpl(private val apiService: ProductService) : UserRemoteDataSource {
    override suspend fun getUser(id: Int): Response<Users> {
        return apiService.getUsersId(id)
    }
}