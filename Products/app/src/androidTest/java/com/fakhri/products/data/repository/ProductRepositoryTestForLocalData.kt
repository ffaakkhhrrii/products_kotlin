package com.fakhri.products.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.local.product.ProductLocalDataSourceImpl
import com.fakhri.products.data.network.api.ApiConfig
import com.fakhri.products.data.network.product.ProductRemoteDataSource
import com.fakhri.products.data.network.product.ProductRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductRepositoryTestForLocalData{
    private lateinit var database: AppDatabase
    private lateinit var dao: FavoriteProductDAO
    private lateinit var productLocalDataSourceImpl: ProductLocalDataSourceImpl
    private lateinit var repository: ProductRepository

    private val remoteDataSource = ProductRemoteDataSourceImpl(ApiConfig().instance)

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.productDao()
        productLocalDataSourceImpl = ProductLocalDataSourceImpl(dao)
        repository = ProductRepository(productLocalDataSourceImpl,remoteDataSource)
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun unFavoriteAndGetProduct() = runBlocking {
        val product1 = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        repository.addToFavorite(product1)
        repository.unFavoriteProduct(product1)
        val isFavorite = repository.isFavorite(1)

        assertFalse(isFavorite)
    }

    @Test
    fun addToFavoriteAndGetProduct() = runBlocking {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        repository.addToFavorite(favoriteProductEntity)
        val isFavorite = repository.isFavorite(1)
        assertTrue(isFavorite)
    }
}