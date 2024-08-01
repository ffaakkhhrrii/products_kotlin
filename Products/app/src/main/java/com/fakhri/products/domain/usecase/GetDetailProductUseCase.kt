package com.fakhri.products.domain.usecase

import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailProductUseCase @Inject constructor(private val repository: IProductRepository){
    suspend operator fun invoke(id: Int): Flow<Resource<DetailProduct>>{
        return repository.getProduct(id)
    }
}