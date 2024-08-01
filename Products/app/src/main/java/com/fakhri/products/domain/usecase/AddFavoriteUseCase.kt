package com.fakhri.products.domain.usecase

import android.util.Log
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.domain.IProductRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(private val repository: IProductRepository) {
    suspend operator fun invoke(favoriteProductEntity: FavoriteProductEntity){
        try {
            repository.addToFavorite(favoriteProductEntity)
        }catch (e: Exception){
            Log.e("AddToFavorite",e.message.toString())
        }
    }
}