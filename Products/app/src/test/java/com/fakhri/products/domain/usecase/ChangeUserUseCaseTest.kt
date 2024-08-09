package com.fakhri.products.domain.usecase

import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import com.fakhri.products.domain.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.math.exp

class ChangeUserUseCaseTest{
    @Mock
    private lateinit var repos: IUserRepository

    private lateinit var changeUserUseCase: ChangeUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        changeUserUseCase = ChangeUserUseCase(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when changeUser usecase invoke, should return Success`() = runTest {
        val user = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val expect = Resource.Success(user)
        `when`(repos.addUser(user)).thenReturn(flowOf(expect))

        val actual = changeUserUseCase(user)
        actual.collect{
            assert(it is Resource.Success)
        }
    }

    @Test
    fun `when changeUser usecase invoke, should return Error`() = runTest {
        val user = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val exception = Exception("Error")
        val expect = Resource.Error(exception.localizedMessage?: "Unknown Error",user)
        `when`(repos.addUser(user)).thenReturn(flowOf(expect))

        val actual = changeUserUseCase(user)
        actual.collect{
            assert(it is Resource.Error)
        }
    }
}