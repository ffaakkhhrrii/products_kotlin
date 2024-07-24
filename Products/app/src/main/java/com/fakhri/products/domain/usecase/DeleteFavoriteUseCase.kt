package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.domain.IProductRepository

class DeleteFavoriteUseCase(private val repository: IProductRepository) {
    suspend operator fun invoke(favoriteProductEntity: FavoriteProductEntity){
        repository.unFavoriteProduct(favoriteProductEntity)
    }
}