package com.fakhri.products.data.network.product

import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.network.api.ProductService
import retrofit2.Response

class DetailProductRemoteDataSourceImpl(
    private val apiService: ProductService
): DetailProductRemoteDataSource {
    override suspend fun getProduct(id: Int): Response<DetailProduct> {
        return apiService.getProductId(id)
    }
}