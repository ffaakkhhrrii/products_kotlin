package com.fakhri.products.domain.usecase

import android.util.Log
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(private val repository: IProductRepository) {
    suspend operator fun invoke(favoriteProductEntity: FavoriteProductEntity): Flow<Resource<FavoriteProductEntity>>{
        return repository.addToFavorite(favoriteProductEntity)
    }
}