package com.fakhri.products.data.network.product

import com.fakhri.products.data.network.response.detail.DetailProduct
import retrofit2.Response

interface DetailProductRemoteDataSource {
    suspend fun getProduct(id: Int): Response<DetailProduct>
}