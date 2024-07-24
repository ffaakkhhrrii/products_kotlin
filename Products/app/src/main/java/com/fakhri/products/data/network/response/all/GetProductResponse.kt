package com.fakhri.products.data.network.response.all

data class GetProductResponse(
    val limit: Int = 0,
    val products: List<Product> = listOf(),
    val skip: Int = 0,
    val total: Int = 0
)