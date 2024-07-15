package com.fakhri.products.repository.product

import androidx.paging.PagingData
import com.fakhri.products.data.local.db.product.FavoriteProduct
import com.fakhri.products.data.network.model.all.Product
import com.fakhri.products.data.network.model.detail.DetailProduct
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    fun getData(): Flow<PagingData<Product>>

    //suspend fun updateProduct(id: Int): Flow<FavoriteProduct>
    suspend fun addToFavorite(favoriteProduct: FavoriteProduct)
    suspend fun getProduct(id: Int): Flow<Result<DetailProduct>>
    suspend fun unFavoriteProduct(favoriteProduct: FavoriteProduct)
    suspend fun isFavorite(id: Int): Boolean

    fun getUserById(id: Int): Flow<Result<Users>>
}