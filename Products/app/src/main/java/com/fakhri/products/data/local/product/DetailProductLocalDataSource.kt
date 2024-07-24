package com.fakhri.products.data.local.product

import androidx.paging.PagingSource
import com.fakhri.products.data.local.db.product.FavoriteProductEntity

interface DetailProductLocalDataSource {
    fun getProduct(id: Int): FavoriteProductEntity
    suspend fun saveProduct(favoriteProductEntity: FavoriteProductEntity)
    suspend fun deleteProduct(favoriteProductEntity: FavoriteProductEntity)
    fun getAllProduct(): PagingSource<Int,FavoriteProductEntity>
}