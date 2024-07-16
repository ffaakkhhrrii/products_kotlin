package com.fakhri.products.data.local.db.product

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fakhri.products.data.network.model.detail.Dimensions
import com.fakhri.products.data.network.model.detail.Meta
import com.fakhri.products.data.network.model.detail.Review
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_favorite")
data class FavoriteProduct(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "availabilityStatus", defaultValue = "")
    val availabilityStatus: String = "",
    @ColumnInfo(name = "brand", defaultValue = "")
    val brand: String = "",
    @ColumnInfo(name = "category", defaultValue = "")
    val category: String = "",
    @ColumnInfo(name = "description", defaultValue = "")
    val description: String = "",
    @Embedded(prefix = "dimensions_")
    val dimensions: Dimensions = Dimensions(),
    @ColumnInfo(name = "discountPercentage", defaultValue = "0.0")
    val discountPercentage: Double = 0.0,
    @ColumnInfo(name = "images", defaultValue = "[]")
    @SerializedName("images")
    val images: ArrayList<String> = arrayListOf(),
    @Embedded(prefix = "meta_")
    val meta: Meta = Meta(),
    @ColumnInfo(name = "minimumOrderQuantity", defaultValue = "0")
    val minimumOrderQuantity: Int = 0,
    @ColumnInfo(name = "price", defaultValue = "0.0")
    val price: Double = 0.0,
    @ColumnInfo(name = "rating", defaultValue = "0.0")
    val rating: Double = 0.0,
    @ColumnInfo(name = "returnPolicy", defaultValue = "")
    val returnPolicy: String = "",
    @ColumnInfo(name = "reviews", defaultValue = "[]")
    @SerializedName("reviews")
    val reviews: ArrayList<Review> = arrayListOf(),
    @ColumnInfo(name = "shippingInformation", defaultValue = "")
    val shippingInformation: String = "",
    @ColumnInfo(name = "sku", defaultValue = "")
    val sku: String = "",
    @ColumnInfo(name = "stock", defaultValue = "0")
    val stock: Int = 0,
    @ColumnInfo(name = "tags", defaultValue = "[]")
    @SerializedName("tags")
    val tags: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "thumbnail", defaultValue = "")
    val thumbnail: String = "",
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String = "",
    @ColumnInfo(name = "warrantyInformation", defaultValue = "")
    val warrantyInformation: String = "",
    @ColumnInfo(name = "weight", defaultValue = "0")
    val weight: Int = 0
)
