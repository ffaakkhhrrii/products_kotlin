package com.fakhri.products.domain.usecase

import androidx.paging.PagingData
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<PagingData<Product>>{
        return repository.getData()
    }
}