package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AddFavoriteUseCaseTest{
    @Mock
    private lateinit var repos: IProductRepository

    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        addFavoriteUseCase = AddFavoriteUseCase(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when addFavorite usecase invoke, should return Success`() = runTest {
        val data = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        val expect = Resource.Success(data)
        Mockito.`when`(repos.addToFavorite(data)).thenReturn(flowOf(expect))

        val actual = repos.addToFavorite(data)
        actual.collect{
            assert(it is Resource.Success)
        }
    }

    @Test
    fun `when addFavorite usecase invoke, should return Error`() = runTest {
        val data = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        val exception = Exception("Error")
        val expect = Resource.Error(exception.localizedMessage?: "Unknown Error",data)
        Mockito.`when`(repos.addToFavorite(data)).thenReturn(flowOf(expect))

        val actual = repos.addToFavorite(data)
        actual.collect{
            assert(it is Resource.Error)
        }
    }
}