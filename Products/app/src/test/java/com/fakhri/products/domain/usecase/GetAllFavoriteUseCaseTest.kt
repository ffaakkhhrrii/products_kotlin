package com.fakhri.products.domain.usecase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.paging.ProductPagingAdapter
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.domain.IProductRepository
import com.fakhri.products.ui.fragment.favorite.FavoriteAdapter
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetAllFavoriteUseCaseTest {

    @Mock
    private lateinit var productRepository: IProductRepository

    private lateinit var getAllFavoriteUseCase: GetAllFavoriteUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        getAllFavoriteUseCase = GetAllFavoriteUseCase(productRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAllFavoriteProduct usecase invoke, should return same data`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        val expect = PagingData.from(listOf(favoriteProductEntity))
        Mockito.`when`(productRepository.getAllFavorite()).thenReturn(flowOf(expect))

        val differ = AsyncPagingDataDiffer(
            diffCallback = FavoriteAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        val paging = getAllFavoriteUseCase()

        paging.collect{
            differ.submitData(it)
            assertEquals(favoriteProductEntity,differ.getItem(0))
            println(differ.getItem(0).toString())
        }
    }
}

private val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}