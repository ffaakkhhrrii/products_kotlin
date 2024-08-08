package com.fakhri.products.di

import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.product.ProductLocalDataSource
import com.fakhri.products.data.local.product.ProductLocalDataSourceImpl
import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.product.ProductRemoteDataSource
import com.fakhri.products.data.network.product.ProductRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductDataSourceModule {

    @Provides
    @Singleton
    fun provideProductLocalDataSource(dao: FavoriteProductDAO): ProductLocalDataSource{
        return ProductLocalDataSourceImpl(
            dao
        )
    }

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(apiService: ProductService): ProductRemoteDataSource{
        return ProductRemoteDataSourceImpl(
            apiService
        )
    }
}