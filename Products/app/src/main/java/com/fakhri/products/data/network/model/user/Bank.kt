package com.fakhri.products.data.network.model.user

data class Bank(
    val cardExpire: String = "",
    val cardNumber: String = "",
    val cardType: String = "",
    val currency: String = "",
    val iban: String = ""
)