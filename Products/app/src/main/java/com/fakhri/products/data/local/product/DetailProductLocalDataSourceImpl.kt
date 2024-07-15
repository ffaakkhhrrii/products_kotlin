package com.fakhri.products.data.local.product


import com.fakhri.products.data.local.db.product.FavoriteProduct
import com.fakhri.products.data.local.db.product.FavoriteProductDAO

class DetailProductLocalDataSourceImpl(private val dao: FavoriteProductDAO) :
    DetailProductLocalDataSource {
    override fun getProduct(id:Int): FavoriteProduct {
        return dao.getProduct(id)
    }

    override suspend fun saveProduct(favoriteProduct: FavoriteProduct) {
        dao.insertProduct(favoriteProduct)
    }

    override suspend fun deleteProduct(favoriteProduct: FavoriteProduct) {
        dao.deleteProduct(favoriteProduct)
    }
}