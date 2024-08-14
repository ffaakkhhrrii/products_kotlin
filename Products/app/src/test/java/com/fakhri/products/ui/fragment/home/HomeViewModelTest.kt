package com.fakhri.products.ui.fragment.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.fakhri.products.data.network.paging.ProductPagingAdapter
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HomeViewModelTest{

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var getProductsUseCase: GetProductsUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(getProductsUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `given state homefragment is Idle` () = runTest {
        val actual = viewModel.state.value
        assertEquals(HomeDefaultState.Idle,actual)
    }

    @Test
    fun`when init paging products return not empty`() = runTest {
        val product1 = Product(
            1,
            listOf("https://example.com/image1.png"),
            200.0,
            listOf("Makanan"),
            "Bakso"
        )

        val expect = PagingData.from(listOf(product1))
        `when`(getProductsUseCase()).thenReturn(flowOf(expect))

        val differ = AsyncPagingDataDiffer(
            diffCallback = ProductPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        viewModel.pagingDataFlow.test {
            differ.submitData(awaitItem())
            assertNotNull(differ.snapshot())
            assertEquals(listOf(product1).size,differ.snapshot().size)
            println(differ.getItem(0).toString())
        }
    }

    @Test
    fun`when FetchProducts should return not empty`() = runTest {
        val product1 = Product(
            1,
            listOf("https://example.com/image1.png"),
            200.0,
            listOf("Makanan"),
            "Bakso"
        )

        val expect = PagingData.from(listOf(product1))
        `when`(getProductsUseCase()).thenReturn(flowOf(expect))

        val differ = AsyncPagingDataDiffer(
            diffCallback = ProductPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        viewModel.processAction(HomeAction.FetchProducts)

        viewModel.pagingDataFlow.test {
            differ.submitData(awaitItem())
            assertEquals(1,differ.snapshot().size)
        }
    }

    @Test
    fun `when onClickProduct should trigger NavigateToDetail effect`() = runTest {
        val id = 1
        viewModel.effect.test {
            viewModel.processAction(HomeAction.OnClickProduct(id))

            val effect = awaitItem()
            assertEquals(HomeEffect.NavigateToDetail(id),effect)
        }
    }

    @Test
    fun `when onClickButtonFavorite should trigger NavigateToFavorite`()= runTest {
        viewModel.effect.test {
            viewModel.processAction(HomeAction.OnClickButtonFavorite)

            val effect = awaitItem()
            assertEquals(HomeEffect.NavigateToFavorite,effect)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}