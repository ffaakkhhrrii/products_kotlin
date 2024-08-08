package com.fakhri.products.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.local.product.ProductLocalDataSource
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.network.paging.ProductsPagingSource
import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.product.ProductRemoteDataSource
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.IProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ProductRepository @Inject constructor(
    private val localDataSource: ProductLocalDataSource,
    private val remoteDataSource: ProductRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IProductRepository {
    override fun getData(): Flow<PagingData<Product>>{
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { ProductsPagingSource(remoteDataSource) },
        ).flow.flowOn(dispatcher)
    }

    override suspend fun addToFavorite(favoriteProductEntity: FavoriteProductEntity) : Flow<Resource<FavoriteProductEntity>> {
        return flow {
            emit(Resource.Loading())
            try {
                localDataSource.saveProduct(favoriteProductEntity)
                emit(Resource.Success(favoriteProductEntity))
            }catch (e: Exception){
                Log.e("AddToFavorite",e.message.toString())
                emit(Resource.Error(e.localizedMessage?: "Unknown Error"))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getProduct(id: Int): Flow<Resource<DetailProduct>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(getProductFromAPI(id)))
        } catch (e: Throwable) {
            emit(Resource.Error(message = e.localizedMessage?: "Unknown Error"))
        }
    }.flowOn(dispatcher)

    override suspend fun unFavoriteProduct(favoriteProductEntity: FavoriteProductEntity): Flow<Resource<FavoriteProductEntity>> {
        return flow {
            emit(Resource.Loading())
            try {
                localDataSource.deleteProduct(favoriteProductEntity)
                Log.i("DeleteFromFavorite","Product is deleted")
                emit(Resource.Success(favoriteProductEntity))
            }catch (e: Exception){
                Log.e("DeleteFromFavorite",e.message.toString())
                emit(Resource.Error(e.localizedMessage?: "Unknown Error"))
            }
        }.flowOn(dispatcher)
    }

    override fun isFavorite(id: Int): Boolean {
        return localDataSource.getProduct(id) != null
    }

    override fun getAllFavorite(): Flow<PagingData<FavoriteProductEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { localDataSource.getAllProduct() },
        ).flow.flowOn(dispatcher)
    }

    private suspend fun getProductFromAPI(id: Int): DetailProduct {
        var product: DetailProduct? = null
        try {
            val response = remoteDataSource.getProduct(id)
            val body = response.body()
            if (body != null) {
                product = body
            }
        } catch (e: Exception) {
            Log.i("Products", e.message.toString())
        }
        return product!!
    }

}