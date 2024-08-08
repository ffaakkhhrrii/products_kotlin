package com.fakhri.products.data.network.response.user

import kotlinx.serialization.Serializable

@Serializable
data class Hair(
    val color: String = "",
    val type: String = ""
)