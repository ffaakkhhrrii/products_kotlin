package com.fakhri.products.network

import com.fakhri.products.data.model.all.GetProductResponse
import com.fakhri.products.data.model.detail.GetProductIdResponse
import com.fakhri.products.data.model.user.GetUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int = 10,
        @Query("select") select:String
    ): Response<GetProductResponse>

    @GET("products/{id}")
    suspend fun getProductId(
        @Path("id") id: Int
    ): Response<GetProductIdResponse>

    @GET("users/{id}")
    suspend fun getUsersId(
        @Path("id") id: Int
    ): Response<GetUserResponse>

}