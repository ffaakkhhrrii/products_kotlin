package com.fakhri.products.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fakhri.products.data.model.all.Product
import com.fakhri.products.data.model.detail.GetProductIdResponse
import com.fakhri.products.data.paging.ProductsPagingSource
import com.fakhri.products.data.utils.Result
import com.fakhri.products.network.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ProductRepository(private val apiService: ProductService) {

    fun getData(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { ProductsPagingSource(apiService) },
        ).flow
    }

    fun getDataById(id: Int) = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getProductId(id)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
            } else {
                emit(Result.Failure(Exception("Error: ${response.code()} ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun getUser(id: Int) = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getUsersId(id)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
            } else {
                emit(Result.Failure(Exception("Error: ${response.code()} ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

}