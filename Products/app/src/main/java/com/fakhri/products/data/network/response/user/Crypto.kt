package com.fakhri.products.data.network.response.user

import kotlinx.serialization.Serializable

@Serializable
data class Crypto(
    val coin: String = "",
    val network: String = "",
    val wallet: String = ""
)