package com.fakhri.products.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.turbine.test
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.local.product.ProductLocalDataSource
import com.fakhri.products.data.network.product.ProductRemoteDataSource
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryTest {

    private fun <T> BDDMockito.BDDMyOngoingStubbing<T>.willThrowUnchecked(vararg throwables: Throwable) {
        var invocationNumber = 0
        this.willAnswer {
            val throwableIndex = invocationNumber++.coerceAtMost(throwables.size - 1)
            throw throwables[throwableIndex]
        }
    }

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repos: ProductRepository
    private val localDataSource: ProductLocalDataSource = mock()
    private val remoteDataSource: ProductRemoteDataSource = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repos = ProductRepository(localDataSource, remoteDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getData should return not null`() = runTest {
        val result = repos.getData().first()
        assertNotNull(result)
    }

    @Test
    fun `when getAllFavorite should return not null`() = runTest(testDispatcher) {
        val product1 = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        `when`(localDataSource.getAllProduct()).thenReturn(MemoryPagingSource(listOf(product1)))

        val result = repos.getAllFavorite().first()
        assertNotNull(result)
    }

    @Test
    fun `when getProduct should return Loading then Success`() =
        runTest {
            val data = DetailProduct(
                id = 1,
                images = arrayListOf("https://example.com/image1.png"),
                price = 10.0,
                tags = arrayListOf("tag1", "tag2"),
                title = "Product 1"
            )

            `when`(remoteDataSource.getProduct(1)).thenReturn(Response.success(data))

            val actual = repos.getProduct(1)
            actual.test {
                val loading = awaitItem()
                assert(loading is Resource.Loading)

                val success = awaitItem()
                assert(success is Resource.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given getProduct should return Loading then Error`() = runTest {
        val exception = Exception("Unknown Error")

        given(remoteDataSource.getProduct(1)).willThrowUnchecked(exception)
        val actual = repos.getProduct(1)
        actual.test {
            val loading = awaitItem()
            assert(loading is Resource.Loading)

            val error = awaitItem()
            assert(error is Resource.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when addToFavorite should return Loading then Success`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        val actual = repos.addToFavorite(favoriteProductEntity)
        actual.test {
            val loading = awaitItem()
            assert(loading is Resource.Loading)

            val success = awaitItem()
            assert(success is Resource.Success)
            cancelAndIgnoreRemainingEvents()
        }

        verify(localDataSource).saveProduct(favoriteProductEntity)
    }

    @Test
    fun `when addToFavorite should Return Error`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        val exception = Exception("Error")
        //doThrow(exception).`when`(localDataSource).saveProduct(favoriteProductEntity)
        given(localDataSource.saveProduct(favoriteProductEntity)).willThrowUnchecked(exception)

        val actual = repos.addToFavorite(favoriteProductEntity)
        actual.test {
            val loading = awaitItem()
            assert(loading is Resource.Loading)

            val error = awaitItem()
            assert(error is Resource.Error)
            cancelAndIgnoreRemainingEvents()
        }
        verify(localDataSource).saveProduct(favoriteProductEntity)
    }

    @Test
    fun `when unFavorite should Return Loading then Success`() = runTest {
        val product1 = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        val actual = repos.unFavoriteProduct(product1)
        actual.test {
            val loading = awaitItem()
            assert(loading is Resource.Loading)

            val success = awaitItem()
            assert(success is Resource.Success)
            cancelAndIgnoreRemainingEvents()
        }
        verify(localDataSource).deleteProduct(product1)
    }


    @Test
    fun `given unFavorite should Return Loading then Error`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        val exception = Exception("Unknown Error")

        //doThrow(exception).`when`(localDataSource).saveProduct(favoriteProductEntity)
        given(localDataSource.deleteProduct(favoriteProductEntity)).willThrowUnchecked(exception)

        val actual = repos.unFavoriteProduct(favoriteProductEntity)
        actual.test {
            val loading = awaitItem()
            assert(loading is Resource.Loading)

            val error = awaitItem()
            assert(error is Resource.Error)
            cancelAndIgnoreRemainingEvents()
        }
        verify(localDataSource).deleteProduct(favoriteProductEntity)
    }

    @Test
    fun `when isFavorite should return false`(): Unit = runTest {

        `when`(localDataSource.getProduct(1)).thenReturn(null)

        val actual = repos.isFavorite(1)

        assertEquals(false, actual)
    }

    @Test
    fun `when isFavorite should return true`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        `when`(localDataSource.getProduct(1)).thenReturn(favoriteProductEntity)

        val actual = repos.isFavorite(1)

        assertEquals(true, actual)
    }

}

class MemoryPagingSource<T: Any>(private val data: List<T>) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }
}