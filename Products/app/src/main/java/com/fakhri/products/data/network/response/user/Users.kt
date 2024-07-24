package com.fakhri.products.data.network.response.user

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fakhri.products.data.local.db.user.UsersEntity

data class Users(
    val id: Int = 0,
    val address: Address = Address(),
    val bank: Bank = Bank(),
    val birthDate: String = "",
    val bloodGroup: String = "",
    val email: String = "",
    val firstName: String = "",
    val image: String = "",
    val lastName: String = "",
    val phone: String = "",
    val role: String = "",
    val university: String = "",
    val username: String = "",
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