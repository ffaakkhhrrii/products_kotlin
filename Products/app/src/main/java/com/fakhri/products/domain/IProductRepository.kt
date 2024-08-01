package com.fakhri.products.domain

import androidx.paging.PagingData
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.network.response.user.Users
import com.fakhri.products.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    fun getData(scope: CoroutineScope): Flow<Resource<PagingData<Product>>>

    //suspend fun updateProduct(id: Int): Flow<FavoriteProduct>
    suspend fun addToFavorite(favoriteProductEntity: FavoriteProductEntity)
    suspend fun getProduct(id: Int): Flow<Resource<DetailProduct>>
    suspend fun unFavoriteProduct(favoriteProductEntity: FavoriteProductEntity)
    fun isFavorite(id: Int): Boolean

    fun getAllFavorite(scope: CoroutineScope): Flow<Resource<PagingData<FavoriteProductEntity>>>
}