package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.repository.UserRepository
import com.fakhri.products.domain.IUserRepository
import javax.inject.Inject

class ChangeUserUseCase @Inject constructor(private val repository: IUserRepository) {
    suspend operator fun invoke(usersEntity: UsersEntity){
        repository.addUser(usersEntity)
    }
}