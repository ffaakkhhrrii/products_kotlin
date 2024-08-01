package com.fakhri.products.di

import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.user.UserLocalDataSource
import com.fakhri.products.data.local.user.UserLocalDataSourceImpl
import com.fakhri.products.data.network.api.ProductService
import com.fakhri.products.data.network.user.UserRemoteDataSource
import com.fakhri.products.data.network.user.UserRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDataSourceModule {

    @Provides
    @Singleton
    fun userLocalDataSource(appDatabase: AppDatabase): UserLocalDataSource{
        return UserLocalDataSourceImpl(
            appDatabase
        )
    }

    @Provides
    @Singleton
    fun userRemoteDataSource(apiService: ProductService): UserRemoteDataSource{
        return UserRemoteDataSourceImpl(
            apiService
        )
    }
}