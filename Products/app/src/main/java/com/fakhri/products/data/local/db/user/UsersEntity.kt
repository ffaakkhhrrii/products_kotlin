package com.fakhri.products.data.local.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_user")
data class UsersEntity(
    @PrimaryKey
    val id: Int = 0,
    val address: String,
    val cardNumber: String,
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