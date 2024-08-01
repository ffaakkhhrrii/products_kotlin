package com.fakhri.products.domain.usecase

import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.domain.IProductRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(private val repository: IProductRepository) {
    operator fun invoke(id: Int): Boolean{
        return repository.isFavorite(id)
    }
}