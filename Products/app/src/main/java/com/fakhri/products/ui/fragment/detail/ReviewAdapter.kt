package com.fakhri.products.ui.fragment.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fakhri.products.data.network.model.detail.Review
import com.fakhri.products.databinding.ItemReviewBinding

class ReviewAdapter: ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(DIFF_CALL_BACK) {

    companion object{
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Review>(){
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(review: Review){
            binding.tvNama.text = review.reviewerEmail
            binding.tvReview.text = review.comment
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}