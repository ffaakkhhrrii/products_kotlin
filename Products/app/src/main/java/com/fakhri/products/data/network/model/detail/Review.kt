package com.fakhri.products.data.network.model.detail

import androidx.room.ColumnInfo

data class Review(
    @ColumnInfo(name = "comment", defaultValue = "")
    val comment: String = "",

    @ColumnInfo(name = "date", defaultValue = "")
    val date: String = "",

    @ColumnInfo(name = "rating", defaultValue = "0")
    val rating: Int = 0,

    @ColumnInfo(name = "reviewerEmail", defaultValue = "")
    val reviewerEmail: String = "",

    @ColumnInfo(name = "reviewerName", defaultValue = "")
    val reviewerName: String = ""
)