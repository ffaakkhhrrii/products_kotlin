package com.fakhri.products.data.local.db.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_favorite")
data class FavoriteProduct(
    @PrimaryKey
    val id: Int
)