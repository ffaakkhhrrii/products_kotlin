package com.fakhri.products.data.network.response.detail

import androidx.room.ColumnInfo

data class Review(
    val comment: String = "",

    val date: String = "",

    val rating: Int = 0,

    val reviewerEmail: String = "",

    val reviewerName: String = ""
)