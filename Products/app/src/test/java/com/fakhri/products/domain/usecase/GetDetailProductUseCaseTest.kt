package com.fakhri.products.domain.usecase

import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetDetailProductUseCaseTest {

    @Mock
    private lateinit var repos: IProductRepository

    private lateinit var getDetailProductUseCase: GetDetailProductUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        getDetailProductUseCase = GetDetailProductUseCase(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getDetailProduct usecase invoke, should return same data and Resource Success`() = runTest {
        val data = DetailProduct(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        val expect = Resource.Success(data)

        `when`(repos.getProduct(1)).thenReturn(flowOf(expect))

        val actual = getDetailProductUseCase(1)
        actual.collect{
            assertEquals(expect.data,it.data)
            assert(it is Resource.Success)
        }
    }

    @Test
    fun `when getDetailProduct usecase invoke, should return Resource Error`() = runTest {
        val data = DetailProduct(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        val exception = Exception("Error")
        val expect = Resource.Error(exception.localizedMessage ?: "Unknown Error",data)

        `when`(repos.getProduct(1)).thenReturn(flowOf(expect))

        val actual = getDetailProductUseCase(1)
        actual.collect{
            assert(it is Resource.Error)
        }
    }
}