package com.fakhri.products.domain.usecase

import androidx.paging.PagingData
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: IProductRepository) {
    operator fun invoke(): Flow<PagingData<Product>>{
        return repository.getData()
    }
}