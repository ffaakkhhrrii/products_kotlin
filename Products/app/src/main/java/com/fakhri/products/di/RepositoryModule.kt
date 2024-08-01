package com.fakhri.products.di

import com.fakhri.products.data.local.product.ProductLocalDataSource
import com.fakhri.products.data.local.user.UserLocalDataSource
import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.product.ProductRemoteDataSource
import com.fakhri.products.data.network.user.UserRemoteDataSource
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.data.repository.UserRepository
import com.fakhri.products.domain.IProductRepository
import com.fakhri.products.domain.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        localDataSource: ProductLocalDataSource,
        remoteDataSource: ProductRemoteDataSource
    ): IProductRepository{
        return ProductRepository(
            localDataSource, remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: UserLocalDataSource,
        remoteDataSource: UserRemoteDataSource
    ): IUserRepository{
        return UserRepository(
            localDataSource,remoteDataSource
        )
    }

}