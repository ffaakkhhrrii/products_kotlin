package com.fakhri.products.data.model.detail

data class GetProductIdResponse(
    val availabilityStatus: String = "",
    val brand: String = "",
    val category: String = "",
    val description: String = "",
    val dimensions: Dimensions = Dimensions(),
    val discountPercentage: Double = 0.0,
    val id: Int = 0,
    val images: List<String> = listOf(),
    val meta: Meta = Meta(),
    val minimumOrderQuantity: Int = 0,
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val returnPolicy: String = "",
    val reviews: List<Review> = listOf(),
    val shippingInformation: String = "",
    val sku: String = "",
    val stock: Int = 0,
    val tags: List<String> = listOf(),
    val thumbnail: String = "",
    val title: String = "",
    val warrantyInformation: String = "",
    val weight: Int = 0
)