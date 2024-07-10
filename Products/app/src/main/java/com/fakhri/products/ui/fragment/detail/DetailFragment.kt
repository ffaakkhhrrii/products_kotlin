package com.fakhri.products.ui.fragment.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.products.MainActivity
import com.fakhri.products.R
import com.fakhri.products.data.ProductRepository
import com.fakhri.products.data.model.detail.GetProductIdResponse
import com.fakhri.products.data.model.detail.Review
import com.fakhri.products.data.paging.ProductPagingAdapter
import com.fakhri.products.data.utils.Result
import com.fakhri.products.databinding.FragmentDetailBinding
import com.fakhri.products.databinding.FragmentHomeBinding
import com.fakhri.products.network.ApiConfig
import com.fakhri.products.ui.fragment.home.HomeFragmentViewModel
import com.fakhri.products.ui.fragment.home.HomeFragmentViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailFragmentViewModel
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = ProductRepository(ApiConfig.instance)
        val factory = DetailFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(DetailFragmentViewModel::class.java)

        binding.btnBack.setOnClickListener{
            //findNavController().navigateUp()
            parentFragmentManager.popBackStack()
        }

        val productId = args.productsId

        viewModel.getDetailData(productId!!)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailData.collect{
                result ->
                when(result){
                    is Result.Loading ->{
                        Log.d("DetailFragment", "Loading data...")
                        binding.pbDetail.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        Log.d("DetailFragment", "Data loaded successfully")
                        setData(result.data)
                        binding.pbDetail.visibility = View.GONE
                    }
                    is Result.Failure ->{
                        Log.d("DetailFragment", "Error loading data: ${result.exception.message}")
                        Toast.makeText(requireActivity(),result.exception.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun getReview(reviews: List<Review>){
        val adapter = ReviewAdapter()
        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        adapter.submitList(reviews)
    }

    private fun setData(data: GetProductIdResponse){
        binding.tvName.text = data.title
        Glide.with(binding.root)
            .load(data.thumbnail)
            .transform(RoundedCorners(20))
            .into(binding.imgProducts)
        val locale = Locale("us","US")
        val currency = NumberFormat.getCurrencyInstance(locale)
        binding.tvPrice.text = currency.format(data.price)
        binding.tvDescrption.text = data.description
        binding.tvRating.text = data.rating.toString()
        getReview(data.reviews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}