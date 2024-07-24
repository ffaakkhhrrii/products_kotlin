package com.fakhri.products.ui.fragment.favorite

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.databinding.ItemProductsBinding
import java.text.NumberFormat
import java.util.Locale

class FavoriteAdapter(private val onClick: (Int)-> Unit) :
    PagingDataAdapter<FavoriteProductEntity, FavoriteAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteProductEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteProductEntity, newItem: FavoriteProductEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteProductEntity, newItem: FavoriteProductEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ProductViewHolder(private val binding: ItemProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: FavoriteProductEntity, onClick: (Int) -> Unit) {
            binding.tvNama.text = product.title
            binding.tvKategori.text = product.tags.joinToString(",")
            val locale = Locale("us","US")
            val currency = NumberFormat.getCurrencyInstance(locale)
            binding.tvHarga.text = currency.format(product.price)

            binding.progressBar.visibility = View.VISIBLE

            Glide.with(itemView.context)
                .load(product.images[0])
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.imgBarang)

            binding.root.setOnClickListener{
                onClick(product.id)
            }
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.onBind(item,onClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

}