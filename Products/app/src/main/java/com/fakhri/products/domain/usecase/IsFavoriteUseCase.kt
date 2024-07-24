package com.fakhri.products.domain.usecase

import com.fakhri.products.data.repository.ProductRepository

class IsFavoriteUseCase(private val repository: ProductRepository) {
    operator fun invoke(id: Int): Boolean{
        return repository.isFavorite(id)
    }
}