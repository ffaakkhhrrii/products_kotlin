package com.fakhri.products.data.network.model.detail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fakhri.products.data.local.db.product.FavoriteProduct


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

fun DetailProduct.toFavoriteProduct(): FavoriteProduct {
    return FavoriteProduct(
        id = this.id,
        availabilityStatus = this.availabilityStatus,
        brand = this.brand,
        category = this.category,
        description = this.description,
        dimensions = this.dimensions,
        discountPercentage = this.discountPercentage,
        images = ArrayList(this.images),
        meta = this.meta,
        minimumOrderQuantity = this.minimumOrderQuantity,
        price = this.price,
        rating = this.rating,
        returnPolicy = this.returnPolicy,
        reviews = ArrayList(this.reviews),
        shippingInformation = this.shippingInformation,
        sku = this.sku,
        stock = this.stock,
        tags = ArrayList(this.tags),
        thumbnail = this.thumbnail,
        title = this.title,
        warrantyInformation = this.warrantyInformation,
        weight = this.weight
    )
}