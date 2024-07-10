package com.fakhri.listbarang.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fakhri.listbarang.data.Barang
import com.fakhri.listbarang.data.BarangResource
import com.fakhri.listbarang.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBarang(BarangResource.getList(this))
    }

    private fun setBarang(barangItem: List<Barang>){
        val adapter = BarangAdapter(object: BarangAdapter.BarangDetail{
            override fun onClick(barang: Barang) {
                val moveActivity = Intent(this@MainActivity,DetailActivity::class.java)
                moveActivity.putExtra("detail_barang",barang.id)
                startActivity(moveActivity)
            }

        })
        adapter.submitList(barangItem)
        binding.rvBarang.adapter = adapter
    }
}