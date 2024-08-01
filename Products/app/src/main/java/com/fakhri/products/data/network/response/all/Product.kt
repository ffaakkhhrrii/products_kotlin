package com.fakhri.products.data.network.response.all

data class Product(
    val id: Int,
    val images: List<String>,
    val price: Double,
    val tags: List<String>,
    val title: String
)