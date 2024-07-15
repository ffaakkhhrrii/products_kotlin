package com.fakhri.products.data.utils

import androidx.room.TypeConverter
import com.fakhri.products.data.network.model.user.Address
import com.fakhri.products.data.network.model.user.Bank
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun fromAddress(address: Address): String{
        return Gson().toJson(address)
    }

    @TypeConverter
    fun toAddress(addressString: String): Address{
        val type = object : TypeToken<Address>() {}.type
        return Gson().fromJson(addressString,type)
    }

    @TypeConverter
    fun fromBank(bank: Bank): String{
        return Gson().toJson(bank)
    }

    @TypeConverter
    fun toBank(bankString: String): Bank{
        val type = object : TypeToken<Bank>() {}.type
        return Gson().fromJson(bankString,type)
    }
}