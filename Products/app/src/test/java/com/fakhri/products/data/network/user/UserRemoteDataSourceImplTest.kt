package com.fakhri.products.data.network.user

import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.response.user.Users
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class UserRemoteDataSourceImplTest {

    private lateinit var userRemoteDataSource: UserRemoteDataSourceImpl
    private val apiService = mock(ProductService::class.java)

    @Before
    fun setUp(){
        userRemoteDataSource = UserRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getUser remote datasource must be the same as result response api`() = runBlocking{
        val expectedUser = readJsonFile<Users>("user.json")
        `when`(apiService.getUsersId(2)).thenReturn(Response.success(expectedUser))

        val actualUser = userRemoteDataSource.getUser(2).body()!!
        assertEquals(expectedUser,actualUser)
    }

    private inline fun <reified T> readJsonFile(fileName: String): T{
        val inputStream = this::class.java.classLoader!!.getResourceAsStream(fileName)
        val json = inputStream.bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}