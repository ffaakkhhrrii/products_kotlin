package com.fakhri.products.domain.usecase

import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class IsFavoriteUseCaseTest{
    @Mock
    private lateinit var repos: IProductRepository

    private lateinit var isFavoriteUseCase: IsFavoriteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        isFavoriteUseCase = IsFavoriteUseCase(repos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when isFavorite usecase invoke, should return true (not null)`() = runTest {

        `when`(repos.isFavorite(1)).thenReturn(true)

        val actual = isFavoriteUseCase(1)
        assertTrue(actual)
    }

    @Test
    fun `when isFavorite usecase invoke, should return false (null)`() = runTest {

        `when`(repos.isFavorite(1)).thenReturn(false)

        val actual = isFavoriteUseCase(1)
        assertFalse(actual)
    }


}