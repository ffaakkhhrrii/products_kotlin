package com.fakhri.listbarang.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.listbarang.R
import com.fakhri.listbarang.data.Barang
import com.fakhri.listbarang.data.BarangResource
import com.fakhri.listbarang.databinding.ActivityDetailBinding
import java.text.NumberFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getId = intent.getStringExtra("detail_barang")
        val product = getBarang(getId!!)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        product?.let {
            Glide.with(this@DetailActivity)
                .load(it.imgBarang)
                .transform(CenterInside(),RoundedCorners(24))
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageViewProduct)

            val localeID = java.util.Locale.getDefault()
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            binding.tvDetailNama.text = it.nama
            binding.tvDetailHarga.text = getString(R.string.harga,formatRupiah.format(it.harga))
            binding.tvDetailDeskripsi.text = getString(R.string.deskripsi,it.deskripsi)
            binding.tvDetailCategory.text = getString(R.string.kategori,it.kategori)
            binding.tvDetailStock.text = getString(R.string.stock,it.stock.toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun getBarang(id: String): Barang?{
        return BarangResource.getList(this).find { it.id == id }
    }
}