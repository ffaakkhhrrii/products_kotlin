package com.fakhri.products.domain.usecase

import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Result
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.flow.Flow

class GetDetailProductUseCase(private val repository: IProductRepository){
    suspend operator fun invoke(id: Int): Flow<Result<DetailProduct>>{
        return repository.getProduct(id)
    }
}