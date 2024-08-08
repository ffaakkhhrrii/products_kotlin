package com.fakhri.products.data.network.response.detail

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val barcode: String = "",
    val createdAt: String = "",
    val qrCode: String = "",
    val updatedAt: String = ""
)