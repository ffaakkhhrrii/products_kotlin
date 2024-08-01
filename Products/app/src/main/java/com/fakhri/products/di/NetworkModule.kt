package com.fakhri.products.di

import com.fakhri.products.data.network.api.ApiConfig
import com.fakhri.products.data.network.api.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitService(
        apiConfig:ApiConfig
    ): ProductService{
        return apiConfig.instance
    }
}