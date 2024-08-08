package com.fakhri.products.data.local.product

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductLocalDataSourceImplTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: FavoriteProductDAO
    private lateinit var productLocalDataSourceImpl: ProductLocalDataSourceImpl

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.productDao()

        productLocalDataSourceImpl = ProductLocalDataSourceImpl(dao)
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun saveAndGetProduct() = runBlocking {
        val product = FavoriteProductEntity(
            12,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            20.0,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            "Bango"
        )
        productLocalDataSourceImpl.saveProduct(product)

        val getProduct = productLocalDataSourceImpl.getProduct(12)

        assertEquals(product,getProduct)
    }

    @Test
    fun deleteAndGetProduct() = runBlocking {
        val product = FavoriteProductEntity(
            12,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            20.0,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            "Bango"
        )
        productLocalDataSourceImpl.saveProduct(product)

        productLocalDataSourceImpl.deleteProduct(product)
        val getProduct = productLocalDataSourceImpl.getProduct(12)
        assertNull(getProduct)
    }

/**
    @Test
    fun testAllProducts() = runBlocking {
        val product1 = FavoriteProductEntity(
            12,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            20.0,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            "Bango"
        )
        val product2 = FavoriteProductEntity(
            13,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            20.0,
            arrayListOf("https://dummyjson.com/icon/michaelw/128","https://dummyjson.com/icon/michaelw/128"),
            "Bango"
        )
        productLocalDataSourceImpl.saveProduct(product1)
        productLocalDataSourceImpl.saveProduct(product2)

        val pagingSource = productLocalDataSourceImpl.getAllProduct()

        val actualData = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        val expectedData = PagingSource.LoadResult.Page(
            data = listOf(product1,product2),
            prevKey = null,
            nextKey = null
        )

        assertEquals(expectedData,actualData)
    }
    **/

}