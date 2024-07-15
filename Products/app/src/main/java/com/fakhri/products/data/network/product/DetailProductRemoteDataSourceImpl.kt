package com.fakhri.products.data.network.product

import com.fakhri.products.data.network.model.detail.DetailProduct
import com.fakhri.products.network.ProductService
import retrofit2.Response

class DetailProductRemoteDataSourceImpl(
    private val apiService: ProductService
): DetailProductRemoteDataSource {
    override suspend fun getProduct(id: Int): Response<DetailProduct> {
        return apiService.getProductId(id)
    }
}