package com.fakhri.products.data.network.model.user

data class Address(
    val address: String = "",
    val city: String = "",
    val coordinates: Coordinates = Coordinates(),
    val country: String = "",
    val postalCode: String = "",
    val state: String = "",
    val stateCode: String = ""
)