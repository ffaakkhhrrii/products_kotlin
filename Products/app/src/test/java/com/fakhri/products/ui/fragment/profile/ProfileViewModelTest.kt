package com.fakhri.products.ui.fragment.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.GetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ProfileViewModelTest{
    private lateinit var viewModel: ProfileViewModel

    @Mock
    private lateinit var getUserUseCase: GetUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(getUserUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `showUser should update state user`() = runTest {
        val data = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val expect = Resource.Success(data)
        `when`(getUserUseCase(1)).thenReturn(flowOf(expect))

        viewModel.processAction(ProfileAction.FetchUser(1))
        viewModel.state.test {
            val iniateState = awaitItem()
            println(iniateState.user.toString())

            val lastState = awaitItem()
            assertTrue(lastState.user is Resource.Success)
            assertEquals(data, (lastState.user as Resource.Success).data)
            println(lastState.user.data.toString())
        }

    }

    @Test
    fun `buttonEditProfile action should trigger NavigateToEditProfile effect`() = runTest {
        viewModel.effect.test {
            viewModel.processAction(ProfileAction.ButtonEditProfilePress(1))

            val effect = awaitItem()
            assert(effect is ProfileEffect.NavigateToEditProfile)
        }
    }

}