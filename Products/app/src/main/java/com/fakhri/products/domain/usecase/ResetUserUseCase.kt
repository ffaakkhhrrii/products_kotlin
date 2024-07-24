package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.repository.UserRepository

class ResetUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int){
        repository.resetUser(id)
    }
}