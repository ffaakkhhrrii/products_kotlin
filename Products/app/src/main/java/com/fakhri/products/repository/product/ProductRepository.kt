package com.fakhri.products.repository.product

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fakhri.products.data.local.product.DetailProductLocalDataSourceImpl
import com.fakhri.products.data.local.db.product.FavoriteProduct
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.network.product.DetailProductRemoteDataSourceImpl
import com.fakhri.products.data.network.model.all.Product
import com.fakhri.products.data.network.model.detail.DetailProduct
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.network.paging.ProductsPagingSource
import com.fakhri.products.data.utils.Result
import com.fakhri.products.network.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository(
    private val apiService: ProductService,
    private val context: Context
): IProductRepository {
    private val dao = AppDatabase.getInstance(context).productDao()
    private val remoteDataSource = DetailProductRemoteDataSourceImpl(apiService)
    private val localDataSource= DetailProductLocalDataSourceImpl(dao)
    override fun getData(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { ProductsPagingSource(apiService) },
        ).flow
    }

    override suspend fun addToFavorite(favoriteProduct: FavoriteProduct) {
        localDataSource.saveProduct(favoriteProduct)
    }

    override suspend fun getProduct(id: Int): Flow<Result<DetailProduct>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(getProductFromAPI(id)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override suspend fun unFavoriteProduct(favoriteProduct: FavoriteProduct) {
        localDataSource.deleteProduct(favoriteProduct)
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return localDataSource.getProduct(id) != null
    }

    suspend fun getProductFromAPI(id: Int):DetailProduct{
        var product: DetailProduct? = null
        try {
            val response = remoteDataSource.getProduct(id)
            val body = response.body()
            if (body != null){
                product = body
            }
        }catch (e: Exception){
            Log.i("Products",e.message.toString())
        }
        return product!!
    }


    override fun getUserById(id: Int): Flow<Result<Users>> = flow {

    }

}