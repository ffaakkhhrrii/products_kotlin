package com.fakhri.products.data.network.response.detail

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class Dimensions(
    val depth: Double = 0.0,
    val height: Double = 0.0,
    val width: Double = 0.0
)
