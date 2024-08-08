package com.fakhri.products.data.network.response.user

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)