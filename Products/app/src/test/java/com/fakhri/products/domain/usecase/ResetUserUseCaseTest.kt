package com.fakhri.products.domain.usecase

import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ResetUserUseCaseTest{
    @Mock
    private lateinit var repos: IUserRepository

    private lateinit var resetUserUseCase: ResetUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        resetUserUseCase = ResetUserUseCase(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when resetUser usecase invoke, should return Resource Success`() = runTest {
        val expect = Resource.Success(Unit)
        `when`(repos.resetUser(1)).thenReturn(flowOf(expect))

        val actual = resetUserUseCase(1)
        actual.collect{
            assert(it is Resource.Success)
        }
    }

    @Test
    fun `when resetUser usecase invoke, should return Resource Error`()= runTest {
        val exception = Exception("Error")
        val expect = Resource.Error(exception.localizedMessage?: "Unknown Error",Unit)
        `when`(repos.resetUser(1)).thenReturn(flowOf(expect))

        val actual = resetUserUseCase(1)
        actual.collect{
            actual.collect{
                assert(it is Resource.Error)
            }
        }
    }

}