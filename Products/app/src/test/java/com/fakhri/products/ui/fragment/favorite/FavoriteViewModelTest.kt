package com.fakhri.products.ui.fragment.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.domain.usecase.GetAllFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FavoriteViewModelTest{
    private lateinit var viewModel: FavoriteViewModel

    @Mock
    private lateinit var getAllFavoriteUseCase: GetAllFavoriteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoriteViewModel(getAllFavoriteUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
    
    @Test
    fun `given state favorite list is Idle`() = runTest {
        val actual = viewModel.state.value
        assertEquals(FavoriteListDefaultState.Idle,actual)
    }

    @Test
    fun `init paging favorite return not empty`()=  runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        val expect = PagingData.from(listOf(favoriteProductEntity))
        Mockito.`when`(getAllFavoriteUseCase()).thenReturn(flowOf(expect))

        val differ = AsyncPagingDataDiffer(
            diffCallback = FavoriteAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        viewModel.pagingDataFlow.test {
            differ.submitData(awaitItem())
            assertNotNull(differ.snapshot())
            assertEquals(listOf(favoriteProductEntity).size,differ.snapshot().size)
            println(differ.getItem(0).toString())
        }
    }

    @Test
    fun `onClickProduct should trigger NavigateToDetail`() = runTest {
        viewModel.effect.test {
            viewModel.processAction(FavoriteListUIAction.OnClickProduct(1))

            val effect = awaitItem()
            assert(effect is FavoriteListUIEffect.NavigateToDetail)
        }
    }

    @Test
    fun `BackButtonPress should trigger BackButtonEffect (Favorite)`() = runTest {
        viewModel.effect.test {
            viewModel.processAction(FavoriteListUIAction.BackButtonPress)

            val effect = awaitItem()
            assert(effect is FavoriteListUIEffect.BackButtonEffect)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}