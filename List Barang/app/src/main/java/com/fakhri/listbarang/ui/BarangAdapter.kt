package com.fakhri.listbarang.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.listbarang.R
import com.fakhri.listbarang.data.Barang
import com.fakhri.listbarang.databinding.ItemBarangBinding
import java.text.NumberFormat

class BarangAdapter(private val listener: BarangDetail): ListAdapter<Barang,BarangAdapter.BarangViewHolder>(DIFF_CALLBACK) {

    class BarangViewHolder(private val binding: ItemBarangBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang,listener: BarangDetail){
            val localeID = java.util.Locale.getDefault()
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            binding.tvNama.text = barang.nama
            binding.tvKategori.text = barang.kategori
            binding.tvHarga.text = formatRupiah.format(barang.harga)
            Glide.with(itemView.context)
                .load(barang.imgBarang)
                .placeholder(R.drawable.ic_launcher_background)
                .transform(FitCenter(),RoundedCorners(10))
                .into(binding.imgBarang)
            binding.root.setOnClickListener{
                listener.onClick(barang)
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Barang>(){
            override fun areItemsTheSame(oldItem: Barang, newItem: Barang): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Barang, newItem: Barang): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val binding = ItemBarangBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        holder.bind(getItem(position),listener)
    }

    interface BarangDetail{
        fun onClick(barang: Barang)
    }

}