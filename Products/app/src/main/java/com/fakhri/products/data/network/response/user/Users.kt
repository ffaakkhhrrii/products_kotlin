package com.fakhri.products.data.network.response.user

import com.fakhri.products.data.local.db.user.UsersEntity
import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val address: Address = Address(),
    val age: Int = 0,
    val bank: Bank = Bank(),
    val birthDate: String = "",
    val bloodGroup: String = "",
    val company: Company = Company(),
    val crypto: Crypto = Crypto(),
    val ein: String = "",
    val email: String = "",
    val eyeColor: String = "",
    val firstName: String = "",
    val gender: String = "",
    val hair: Hair = Hair(),
    val height: Double = 0.0,
    val id: Int = 0,
    val image: String = "",
    val ip: String = "",
    val lastName: String = "",
    val macAddress: String = "",
    val maidenName: String = "",
    val password: String = "",
    val phone: String = "",
    val role: String = "",
    val ssn: String = "",
    val university: String = "",
    val userAgent: String = "",
    val username: String = "",
    val weight: Double = 0.0
)

fun Users.toEntity(): UsersEntity{
    return UsersEntity(
        id = this.id,
        address = this.address.address,
        cardNumber = this.bank.cardNumber,
        birthDate = this.birthDate,
        bloodGroup = this.bloodGroup,
        email = this.email,
        firstName = this.firstName,
        image = this.image,
        lastName = this.lastName,
        phone = this.phone,
        role = this.role,
        university = this.university,
        username = this.username
    )
}