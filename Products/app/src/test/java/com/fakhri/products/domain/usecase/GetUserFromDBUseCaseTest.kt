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
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetUserFromDBUseCaseTest {
    @Mock
    private lateinit var repos: IUserRepository

    private lateinit var getUserFromDBUseCase: GetUserFromDBUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        getUserFromDBUseCase = GetUserFromDBUseCase(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getUserFromDB usecase invoke, should return same data and Resource Success`() = runTest {
        val user = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )
        val expect = Resource.Success(user)
        `when`(repos.getUserFromDB(1)).thenReturn(flowOf(expect))

        val actual = getUserFromDBUseCase(1)
        actual.collect{
            assertEquals(expect.data,it.data)
            assert(it is Resource.Success)
        }
    }

    @Test
    fun `when getUserFromDB usecase invoke, should return Resource Error`() = runTest {
        val user = UsersEntity(
            id = 1,
            firstName = "Fakhri",
            address = "Bumi",
            cardNumber = "123"
        )

        val exception = Exception("Error")
        val expect = Resource.Error(exception.localizedMessage?:"Unknown Error",user)
        `when`(repos.getUserFromDB(1)).thenReturn(flowOf(expect))

        val actual = getUserFromDBUseCase(1)
        actual.collect{
            assert(it is Resource.Error)
        }
    }

}