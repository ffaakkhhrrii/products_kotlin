package com.fakhri.products.domain.usecase

import androidx.paging.PagingData
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavoriteUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<PagingData<FavoriteProductEntity>>{
        return repository.getAllFavorite()
    }
}