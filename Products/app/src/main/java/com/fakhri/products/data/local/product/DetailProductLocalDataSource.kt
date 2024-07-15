package com.fakhri.products.data.local.product

import com.fakhri.products.data.local.db.product.FavoriteProduct

interface DetailProductLocalDataSource {
    fun getProduct(id: Int): FavoriteProduct
    suspend fun saveProduct(favoriteProduct: FavoriteProduct)
    suspend fun deleteProduct(favoriteProduct: FavoriteProduct)
}