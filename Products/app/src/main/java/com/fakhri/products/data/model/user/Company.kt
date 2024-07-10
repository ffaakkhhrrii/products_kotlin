package com.fakhri.products.data.model.user

data class Company(
    val address: Address = Address(),
    val department: String = "",
    val name: String = "",
    val title: String = ""
)