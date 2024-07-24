package com.fakhri.products.data.network.response.all

data class Product(
    val id: Int = 0,
    val images: List<String> = listOf(),
    val price: Double = 0.0,
    val tags: List<String> = listOf(),
    val title: String = ""
)