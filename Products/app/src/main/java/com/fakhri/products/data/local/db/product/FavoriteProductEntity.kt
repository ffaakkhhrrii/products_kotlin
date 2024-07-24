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
    @ColumnInfo(name = "images", defaultValue = "[]")
    @SerializedName("images")
    val images: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "price", defaultValue = "0.0")
    val price: Double = 0.0,
    @ColumnInfo(name = "tags", defaultValue = "[]")
    @SerializedName("tags")
    val tags: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String = ""
)

