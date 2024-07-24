package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.repository.UserRepository

class ChangeUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(usersEntity: UsersEntity){
        repository.addUser(usersEntity)
    }
}