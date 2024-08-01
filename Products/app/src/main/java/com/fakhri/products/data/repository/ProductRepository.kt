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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ProductRepository @Inject constructor(
    private val localDataSource: ProductLocalDataSource,
    private val remoteDataSource: ProductRemoteDataSource,
) : IProductRepository {
    override fun getData(scope: CoroutineScope): Flow<Resource<PagingData<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val pager = Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { ProductsPagingSource(remoteDataSource.getService()) },
            ).flow.cachedIn(scope)
            pager
                .map { pagingData -> Resource.Success(pagingData) }
                .collect { resource -> emit(resource) }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun addToFavorite(favoriteProductEntity: FavoriteProductEntity) {
        try {
            localDataSource.saveProduct(favoriteProductEntity)
            Log.e("AddToFavorite","Product is added")
        }catch (e: Exception){
            Log.e("AddToFavorite",e.message.toString())
        }
    }

    override suspend fun getProduct(id: Int): Flow<Resource<DetailProduct>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(getProductFromAPI(id)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun unFavoriteProduct(favoriteProductEntity: FavoriteProductEntity) {
        localDataSource.deleteProduct(favoriteProductEntity)
    }

    override fun isFavorite(id: Int): Boolean {
        return localDataSource.getProduct(id) != null
    }

    override fun getAllFavorite(scope: CoroutineScope): Flow<Resource<PagingData<FavoriteProductEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val pager = Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { localDataSource.getAllProduct() },
            ).flow.cachedIn(scope)
            pager
                .map { pagingData -> Resource.Success(pagingData) }
                .collect { resource -> emit(resource) }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    suspend fun getProductFromAPI(id: Int): DetailProduct {
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