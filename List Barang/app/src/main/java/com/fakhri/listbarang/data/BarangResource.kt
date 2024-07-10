package com.fakhri.listbarang.data

import android.content.Context
import com.fakhri.listbarang.R

object BarangResource {
    fun getList(context: Context): ArrayList<Barang> {
        val id = context.resources.getStringArray(R.array.product_ids)
        val dataName = context.resources.getStringArray(R.array.product_names)
        val dataDescription = context.resources.getStringArray(R.array.product_descriptions)
        val dataCategory = context.resources.getStringArray(R.array.product_categories)
        val dataHarga = context.resources.getIntArray(R.array.product_prices)
        val dataStock = context.resources.getIntArray(R.array.product_stocks)
        val dataImages = context.resources.obtainTypedArray(R.array.product_images)
        val list = ArrayList<Barang>()
        for (i in id.indices) {
            val barang = Barang(
                id[i],
                dataName[i],
                dataDescription[i],
                dataCategory[i],
                dataHarga[i].toLong(),
                dataStock[i],
                dataImages.getResourceId(i, -1)
            )
            list.add(barang)
        }
        return list
    }
}