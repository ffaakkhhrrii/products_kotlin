package com.fakhri.products.data.model.detail

data class Review(
    val comment: String = "",
    val date: String = "",
    val rating: Int = 0,
    val reviewerEmail: String = "",
    val reviewerName: String = ""
)