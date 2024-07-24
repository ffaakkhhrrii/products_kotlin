package com.fakhri.products.data.network.api

import com.fakhri.products.data.network.response.all.GetProductResponse
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.network.response.user.Users
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
    ): Response<DetailProduct>

    @GET("users/{id}")
    suspend fun getUsersId(
        @Path("id") id: Int
    ): Response<Users>

}