package com.fakhri.products.data.network.product

import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.response.all.GetProductResponse
import retrofit2.Response
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(
    private val apiService: ProductService
): ProductRemoteDataSource {
    override suspend fun getProduct(id: Int): Response<DetailProduct> {
        return apiService.getProductId(id)
    }

    override suspend fun getProducts(
        skip: Int,
        limit: Int,
        select: String
    ): Response<GetProductResponse> {
        return apiService.getProducts(skip, limit, select)
    }
}