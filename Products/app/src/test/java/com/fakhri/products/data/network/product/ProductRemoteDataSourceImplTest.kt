package com.fakhri.products.data.network.product

import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.response.all.GetProductResponse
import com.fakhri.products.data.network.response.detail.DetailProduct
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

class ProductRemoteDataSourceImplTest {

    private lateinit var productRemoteDataSource: ProductRemoteDataSourceImpl
    private val apiService: ProductService = mock(ProductService::class.java)

    @Before
    fun setup() {
        productRemoteDataSource = ProductRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getProducts remote datasource must be the same as result response api`() = runTest {
        val expectedProducts = readJsonFile<GetProductResponse>("products.json")
        `when`(apiService.getProducts(0, 10, "title,price,tags,images")).thenReturn(Response.success(expectedProducts))

        val actualProducts = productRemoteDataSource.getProducts(0, 10, "title,price,tags,images").body()!!

        assertEquals(expectedProducts, actualProducts)
    }

    @Test
    fun `getProduct remote datasource must be the same as result response api`() = runTest {
        val expectedProduct = readJsonFile<DetailProduct>("detail_product.json")
        `when`(apiService.getProductId(1)).thenReturn(Response.success(expectedProduct))
        val actualDetail = productRemoteDataSource.getProduct(1).body()!!
        assertEquals(expectedProduct,actualDetail)
    }

    private inline fun <reified T>readJsonFile(fileName: String): T {
        val inputStream = this::class.java.classLoader!!.getResourceAsStream(fileName)!!
        val json = inputStream.bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }

}
