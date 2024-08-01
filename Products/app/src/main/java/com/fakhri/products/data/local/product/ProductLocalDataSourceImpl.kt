package com.fakhri.products.data.local.product


import androidx.paging.PagingSource
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val database: AppDatabase
) : ProductLocalDataSource {
    private val dao = database.productDao()
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