package com.fakhri.products.data.network.product

import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.response.all.GetProductResponse
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.network.response.detail.DetailProduct
import retrofit2.Response

interface ProductRemoteDataSource {
    suspend fun getProduct(id: Int): Response<DetailProduct>
    suspend fun getProducts(skip: Int,limit: Int,select: String): Response<GetProductResponse>
}