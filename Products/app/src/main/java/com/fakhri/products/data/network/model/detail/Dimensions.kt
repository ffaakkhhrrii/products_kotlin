package com.fakhri.products.data.network.model.detail

import androidx.room.ColumnInfo

data class Dimensions(
    @ColumnInfo(name = "depth", defaultValue = "0.0")
    val depth: Double = 0.0,

    @ColumnInfo(name = "height", defaultValue = "0.0")
    val height: Double = 0.0,

    @ColumnInfo(name = "width", defaultValue = "0.0")
    val width: Double = 0.0
)
