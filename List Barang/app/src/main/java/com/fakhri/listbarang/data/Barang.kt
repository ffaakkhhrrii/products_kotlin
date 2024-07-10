package com.fakhri.listbarang.data

data class Barang(
    val id : String,
    val nama: String,
    val deskripsi: String,
    val kategori: String,
    val harga: Long,
    val stock: Int,
    val imgBarang: Int
)