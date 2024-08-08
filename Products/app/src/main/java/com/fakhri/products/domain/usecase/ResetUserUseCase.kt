package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.repository.UserRepository
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetUserUseCase @Inject constructor(private val repository: IUserRepository) {
    suspend operator fun invoke(id: Int): Flow<Resource<Unit>>{
        return repository.resetUser(id)
    }
}