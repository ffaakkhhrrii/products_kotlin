package com.fakhri.products.data.local.db.product

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(favoriteProductEntity: FavoriteProductEntity)

    @Delete
    suspend fun deleteProduct(favoriteProductEntity: FavoriteProductEntity)

    @Query("SELECT * FROM tb_favorite WHERE id = :id")
    fun getProduct(id: Int): FavoriteProductEntity

    @Query("SELECT * FROM tb_favorite")
    fun getAllFavoriteProduct(): PagingSource<Int,FavoriteProductEntity>

}