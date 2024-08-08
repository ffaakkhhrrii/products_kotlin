package com.fakhri.products.data.local.user

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fakhri.products.data.local.db.AppDatabase
import com.fakhri.products.data.local.db.product.FavoriteProductDAO
import com.fakhri.products.data.local.db.user.UserDAO
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.local.product.ProductLocalDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserLocalDataSourceImplTest{
    private lateinit var database: AppDatabase
    private lateinit var dao: UserDAO
    private lateinit var userLocalDataSourceImpl: UserLocalDataSourceImpl

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.userDao()

        userLocalDataSourceImpl = UserLocalDataSourceImpl(dao)
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun saveAndGetUser() = runBlocking{
        val user = UsersEntity(
            id = 1,
            address = "Bumi",
            cardNumber = "12345678",
            birthDate = "1990/10/10",
            bloodGroup = "A-",
            email = "tes@example.com",
            firstName = "Fakhri",
            lastName = "Zain",
            image = "https://dummyjson.com/icon/michaelw/128",
            phone = "0878",
            role = "admin",
            university = "SMKN 26",
            username = "ffaakhriii"
        )

        userLocalDataSourceImpl.addUser(user)

        val getUser = userLocalDataSourceImpl.getUser(1)

        assertEquals(user,getUser)
    }

    @Test
    fun deleteAndGetUser()= runBlocking {
        val user = UsersEntity(
            id = 1,
            address = "Bumi",
            cardNumber = "12345678",
            birthDate = "1990/10/10",
            bloodGroup = "A-",
            email = "tes@example.com",
            firstName = "Fakhri",
            lastName = "Zain",
            image = "https://dummyjson.com/icon/michaelw/128",
            phone = "0878",
            role = "admin",
            university = "SMKN 26",
            username = "ffaakhriii"
        )

        userLocalDataSourceImpl.addUser(user)
        userLocalDataSourceImpl.deleteUser(1)

        val getUser = userLocalDataSourceImpl.getUser(1)

        assertNull(getUser)
    }

}