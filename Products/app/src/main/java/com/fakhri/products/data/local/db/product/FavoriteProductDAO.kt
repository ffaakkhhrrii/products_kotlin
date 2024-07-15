package com.fakhri.products.data.local.db.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(favoriteProduct: FavoriteProduct)

    @Delete
    suspend fun deleteProduct(favoriteProduct: FavoriteProduct)

    @Query("SELECT * FROM tb_favorite WHERE id = :id")
    fun getProduct(id: Int): FavoriteProduct
}