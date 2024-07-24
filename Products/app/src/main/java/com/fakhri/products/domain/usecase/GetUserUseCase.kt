package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.repository.UserRepository
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(private val repository: UserRepository) {
    operator fun invoke(id: Int): Flow<Result<UsersEntity>>{
        return repository.getUser(id)
    }
}