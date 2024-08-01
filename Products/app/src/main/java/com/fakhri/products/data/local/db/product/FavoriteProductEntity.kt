package com.fakhri.products.data.local.db.product

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fakhri.products.data.network.response.detail.Dimensions
import com.fakhri.products.data.network.response.detail.Review
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_favorite")
data class FavoriteProductEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "images")
    @SerializedName("images")
    val images: ArrayList<String>,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "tags")
    @SerializedName("tags")
    val tags: ArrayList<String>,
    @ColumnInfo(name = "title")
    val title: String
)