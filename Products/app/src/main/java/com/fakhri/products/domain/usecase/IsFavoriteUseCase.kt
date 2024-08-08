package com.fakhri.products.domain.usecase

import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(private val repository: IProductRepository) {
    operator fun invoke(id: Int): Boolean {
        return repository.isFavorite(id)
    }
}