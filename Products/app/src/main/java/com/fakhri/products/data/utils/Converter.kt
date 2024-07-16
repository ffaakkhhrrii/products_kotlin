package com.fakhri.products.data.utils

import androidx.room.TypeConverter
import com.fakhri.products.data.network.model.detail.Review
import com.fakhri.products.data.network.model.user.Address
import com.fakhri.products.data.network.model.user.Bank
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConverterReview {
    @TypeConverter
    fun fromString(value: String): ArrayList<Review> {
        val listType = object : TypeToken<ArrayList<Review>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<Review>): String {
        return Gson().toJson(list)
    }
}

class ConverterList{
    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: ArrayList<String>): String {
        return Gson().toJson(list)
    }
}