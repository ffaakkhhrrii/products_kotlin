package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.ChangeUserUseCase
import com.fakhri.products.domain.usecase.GetUserFromDBUseCase
import com.fakhri.products.domain.usecase.ResetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class EditProfileViewModelTest{
    private lateinit var viewModel: EditProfileViewModel

    @Mock
    private lateinit var getUserFromDBUseCase: GetUserFromDBUseCase

    @Mock
    private lateinit var changeUserUseCase: ChangeUserUseCase

    @Mock
    private lateinit var resetUserUseCase: ResetUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = EditProfileViewModel(getUserFromDBUseCase, changeUserUseCase, resetUserUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUser should update state user`() = runTest {
        val data = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val expect = Resource.Success(data)
        `when`(getUserFromDBUseCase.invoke(1)).thenReturn(flowOf(expect))

        viewModel.processAction(EditProfileUIAction.FetchUser(1))

        viewModel.state.test {
            val initiateState = awaitItem()
            println(initiateState.user.toString())

            val updateState = awaitItem()
            assert(updateState.user is Resource.Success)
            assertEquals(expect.data,updateState.user.data)
            println(updateState.user.data.toString())
        }
    }

    @Test
    fun `changeUser should trigger state user and change`() = runTest {
        val data = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val expect = Resource.Success(data)
        `when`(changeUserUseCase.invoke(data)).thenReturn(flowOf(expect))

        viewModel.processAction(EditProfileUIAction.ChangeUser(data))

        viewModel.state.test {
            val initiateState = awaitItem()
            println(initiateState.user.toString())
            println(initiateState.change.toString())

            val updateState = awaitItem()
            assert(updateState.user is Resource.Success)
            assert(updateState.change is Resource.Success)
            assertEquals(expect.data,updateState.user.data)
            println(updateState.user.data.toString())
        }
    }

    @Test
    fun`changeUser should trigger BackToEditProfile effect`() = runTest {
        val data = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val expect = Resource.Success(data)
        `when`(changeUserUseCase.invoke(data)).thenReturn(flowOf(expect))

        viewModel.effect.test {
            viewModel.processAction(EditProfileUIAction.ChangeUser(data))

            val effect = awaitItem()
            assert(effect is EditProfileUIEffect.BackToProfile)
        }
    }

    @Test
    fun `resetUser should trigger state reset`() = runTest {
        val expect = Resource.Success(Unit)
        `when`(resetUserUseCase.invoke(1)).thenReturn(flowOf(expect))

        viewModel.processAction(EditProfileUIAction.ResetUser(1))

        viewModel.state.test {
            val initiateState = awaitItem()
            println(initiateState.reset.toString())

            val updateState = awaitItem()
            assert(updateState.reset is Resource.Success)
        }
    }

    @Test
    fun`resetUser should trigger BackToEditProfile effect`() = runTest {
        val expect = Resource.Success(Unit)
        `when`(resetUserUseCase.invoke(1)).thenReturn(flowOf(expect))

        viewModel.effect.test {
            viewModel.processAction(EditProfileUIAction.ResetUser(1))

            val effect = awaitItem()
            assert(effect is EditProfileUIEffect.BackToProfile)
        }
    }

    @Test
    fun `BackButtonPress should trigger BackToProfile`()= runTest {
        viewModel.effect.test {
            viewModel.processAction(EditProfileUIAction.BackButtonPress)

            val effect = awaitItem()
            assert(effect is EditProfileUIEffect.BackToProfile)
        }
    }

    @Test
    fun `ShowGalleryButtonPress should trigger ShowGallery effect`()= runTest {
        viewModel.effect.test {
            viewModel.processAction(EditProfileUIAction.ShowGalleryButtonPressed)

            val effect = awaitItem()
            assert(effect is EditProfileUIEffect.ShowGallery)
        }
    }

    @Test
    fun `ShowDatePickerButtonPress should trigger ShowDatePicker effect`()= runTest {
        viewModel.effect.test {
            viewModel.processAction(EditProfileUIAction.ShowDatePickerButtonPressed)

            val effect = awaitItem()
            assert(effect is EditProfileUIEffect.ShowDatePicker)
        }
    }

    @Test
    fun `ShowCameraButtonPress should trigger ShowCamera effect`()= runTest {
        viewModel.effect.test {
            viewModel.processAction(EditProfileUIAction.ShowCameraButtonPressed)

            val effect = awaitItem()
            assert(effect is EditProfileUIEffect.ShowCamera)
        }
    }
}