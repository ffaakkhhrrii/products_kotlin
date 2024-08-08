package com.fakhri.products.data.network.response.user

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val address: Address = Address(),
    val department: String = "",
    val name: String = "",
    val title: String = ""
)