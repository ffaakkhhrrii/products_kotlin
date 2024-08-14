package com.fakhri.products.ui.fragment.detail

import app.cash.turbine.test
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.AddFavoriteUseCase
import com.fakhri.products.domain.usecase.DeleteFavoriteUseCase
import com.fakhri.products.domain.usecase.GetDetailProductUseCase
import com.fakhri.products.domain.usecase.IsFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @Mock
    private lateinit var getDetailProductUseCase: GetDetailProductUseCase

    @Mock
    private lateinit var isFavoriteUseCase: IsFavoriteUseCase

    @Mock
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    private lateinit var viewModel: DetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(
            getDetailProductUseCase,
            isFavoriteUseCase,
            addFavoriteUseCase,
            deleteFavoriteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetchDetailProduct, should update state product and isFavorite (true)`() = runTest {
        val product = DetailProduct(
            id = 1,
            brand = "Kahf"
        )
        val resource = Resource.Success(product)

        // Ensure that the use case returns the expected flow
        `when`(getDetailProductUseCase.invoke(1)).thenReturn(flowOf(resource))
        `when`(isFavoriteUseCase.invoke(1)).thenReturn(true)

        // Trigger the action to fetch the product
        viewModel.processAction(DetailAction.FetchProduct(1))

        // Collect state changes
        viewModel.state.test {
            // Assert that the initial state is Idle
            val initialState = awaitItem()
            println(initialState.isFavorite.toString())

            val updatedState = awaitItem()
            assertTrue(updatedState.product is Resource.Success)
            assertTrue(updatedState.isFavorite)
            assertEquals(product, (updatedState.product as Resource.Success).data)
            println(updatedState.product.data.toString())
            println(updatedState.isFavorite.toString())
        }
    }

    @Test
    fun `when onClickProduct, should update state addFavorite and isFavorite (true)`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )

        `when`(addFavoriteUseCase.invoke(favoriteProductEntity)).thenReturn(flowOf(Resource.Success(favoriteProductEntity)))
        //`when`(deleteFavoriteUseCase.invoke(favoriteProductEntity)).thenReturn(flowOf(Resource.Success(favoriteProductEntity)))

        viewModel.processAction(DetailAction.OnClickProduct(favoriteProductEntity))

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isFavorite)

            val state2 = awaitItem()
            assert(state2.addFavorite is Resource.Success)
            assertTrue(state2.isFavorite)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onClickProduct, should update state deleteFavorite and isFavorite (false)`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        viewModel._isFavorite.value = true

        //`when`(addFavoriteUseCase.invoke(favoriteProductEntity)).thenReturn(flowOf(Resource.Success(favoriteProductEntity)))
        `when`(deleteFavoriteUseCase.invoke(favoriteProductEntity)).thenReturn(flowOf(Resource.Success(favoriteProductEntity)))

        viewModel.processAction(DetailAction.OnClickProduct(favoriteProductEntity))

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isFavorite)

            val state2 = awaitItem()
            assert(state2.deleteFavorite is Resource.Success)
            assertFalse(state2.isFavorite)
        }
    }

    @Test
    fun `when onClickProduct should trigger ShowMessageBar Effect`() = runTest {
        val favoriteProductEntity = FavoriteProductEntity(
            id = 1,
            images = arrayListOf("https://example.com/image1.png"),
            price = 10.0,
            tags = arrayListOf("tag1", "tag2"),
            title = "Product 1"
        )
        `when`(addFavoriteUseCase.invoke(favoriteProductEntity)).thenReturn(flowOf(Resource.Success(favoriteProductEntity)))

        viewModel.effect.test {
            viewModel.processAction(DetailAction.OnClickProduct(favoriteProductEntity))
            val effect = awaitItem()
            assert(effect is DetailEffect.ShowMessageBar)
            assertEquals("Product's added to favorite",(effect as DetailEffect.ShowMessageBar).message)
        }
    }

    @Test
    fun `when BackButtonPressed should trigger BackButtonEffect`()= runTest {
        viewModel.effect.test {
            viewModel.processAction(DetailAction.BackButtonPressed)

            val effect = awaitItem()
            assert(effect is DetailEffect.BackButtonEffect)
        }
    }
}