package com.fakhri.products.data.network.response.detail

import com.fakhri.products.data.local.db.product.FavoriteProductEntity


data class DetailProduct(
    val id: Int = 0,
    val availabilityStatus: String = "",
    val brand: String = "",
    val category: String = "",
    val description: String = "",
    val dimensions: Dimensions = Dimensions(),
    val discountPercentage: Double = 0.0,
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

fun DetailProduct.toFavoriteProduct(): FavoriteProductEntity {
    return FavoriteProductEntity(
        id = this.id,
        images = ArrayList(this.images),
        price = this.price,
        tags = ArrayList(this.tags),
        title = this.title,
    )
}