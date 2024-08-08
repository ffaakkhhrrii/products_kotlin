package com.fakhri.products.di

import android.content.Context
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.db.user.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase): FavoriteProductDAO{
        return appDatabase.productDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDAO{
        return appDatabase.userDao()
    }
}