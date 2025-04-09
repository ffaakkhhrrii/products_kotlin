package com.fakhri.products.data.network.paging

import androidx.paging.PagingSource
import com.fakhri.products.data.network.product.ProductRemoteDataSource
import com.fakhri.products.data.network.response.all.GetProductResponse
import com.fakhri.products.data.network.response.all.Product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class ProductsPagingSourceTest{
    private val mockRemoteDataSource = mock(ProductRemoteDataSource::class.java)

    private val pagingSource = ProductsPagingSource(mockRemoteDataSource)

    @Test
    fun `load returns page when successful`() = runTest {
        // Arrange
        val products = listOf(
            Product(id = 1, title = "Product 1", price = 10.0, tags = listOf("tag1"), images = listOf("image1")),
            Product(id = 2, title = "Product 2", price = 20.0, tags = listOf("tag2"), images = listOf("image2"))
        )
        val response = GetProductResponse(
            products = products,
            limit = 10,
            skip = 0,
            total = 2
        )
        `when`(mockRemoteDataSource.getProducts(0, 10, "title,price,tags,images"))
            .thenReturn(Response.success(response))

        // Act
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(0, 10, false))

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(products, page.data)
        assertNull(page.prevKey)
        assertEquals(10, page.nextKey)
    }
}