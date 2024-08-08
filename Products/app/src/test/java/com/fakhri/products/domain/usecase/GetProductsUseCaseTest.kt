package com.fakhri.products.domain.usecase

import android.graphics.pdf.PdfDocument.Page
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.fakhri.products.data.network.paging.ProductPagingAdapter
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetProductsUseCaseTest{
    @Mock
    private lateinit var productRepository: IProductRepository

    private lateinit var getProductsUseCase: GetProductsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        getProductsUseCase = GetProductsUseCase(productRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `when getProducts usecase invoke, should return same data`() = runTest {
        val product1 = Product(
            1,
            listOf("https://example.com/image1.png"),
            200.0,
            listOf("Makanan"),
            "Bakso"
        )
        val expect = PagingData.from(listOf(product1))
        `when`(productRepository.getData()).thenReturn(flowOf(expect))

        val differ = AsyncPagingDataDiffer(
            diffCallback = ProductPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        val paging = getProductsUseCase()

        paging.collect{
            differ.submitData(it)
            assertEquals(product1,differ.getItem(0))
        }
    }
}

private val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}