package com.fakhri.products.data.local.product


import androidx.paging.PagingSource
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val dao: FavoriteProductDAO
) : ProductLocalDataSource {
    override fun getProduct(id: Int): FavoriteProductEntity {
        return dao.getProduct(id)
    }

    override suspend fun saveProduct(favoriteProductEntity: FavoriteProductEntity) {
        dao.insertProduct(favoriteProductEntity)
    }

    override suspend fun deleteProduct(favoriteProductEntity: FavoriteProductEntity) {
        dao.deleteProduct(favoriteProductEntity)
    }

    override fun getAllProduct(): PagingSource<Int, FavoriteProductEntity> {
        return dao.getAllFavoriteProduct()
    }
}