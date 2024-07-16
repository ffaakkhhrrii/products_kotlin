package com.fakhri.products.data.local.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fakhri.products.data.local.db.product.FavoriteProduct
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.db.user.UserDAO
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.utils.ConverterList
import com.fakhri.products.data.utils.ConverterReview

@Database(
    exportSchema = true, version = 2, entities = [FavoriteProduct::class,Users::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(ConverterList::class,ConverterReview::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): FavoriteProductDAO
    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "db_product"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}