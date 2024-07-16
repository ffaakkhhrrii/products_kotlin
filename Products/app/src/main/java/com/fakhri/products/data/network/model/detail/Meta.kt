package com.fakhri.products.data.network.model.detail

import androidx.room.ColumnInfo

data class Meta(
    @ColumnInfo(name = "barcode", defaultValue = "")
    val barcode: String = "",

    @ColumnInfo(name = "createdAt", defaultValue = "")
    val createdAt: String = "",

    @ColumnInfo(name = "qrCode", defaultValue = "")
    val qrCode: String = "",

    @ColumnInfo(name = "updatedAt", defaultValue = "")
    val updatedAt: String = ""
)