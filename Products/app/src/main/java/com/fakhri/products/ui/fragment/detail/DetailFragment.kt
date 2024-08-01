package com.fakhri.products.ui.fragment.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.products.R
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.network.response.detail.Review
import com.fakhri.products.data.network.response.detail.toFavoriteProduct
import com.fakhri.products.databinding.FragmentDetailBinding
import com.fakhri.products.data.utils.handleCollect
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailFragmentViewModel by viewModels()
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
        val productId = args.productsId
        val uiAction = {action: DetailAction-> viewModel.processAction(action)}
        uiAction(DetailAction.FetchProduct(productId))
        val productFlow = viewModel.state.map { it.product }
        viewLifecycleOwner.lifecycleScope.launch {
            productFlow.handleCollect(
                onSuccess = {
                    data->
                    Log.d("DetailFragment", "Data loaded successfully")
                    binding.pbDetail.visibility = View.GONE
                    setData(data.data!!)
                    binding.btnFavorite.setOnClickListener {
                        viewLifecycleOwner.lifecycleScope.launch {
                            uiAction(DetailAction.OnClickProduct(data.data.toFavoriteProduct()))
                        }
                    }
                },
                onLoading = {
                    Log.d("DetailFragment", "Loading data...")
                    binding.pbDetail.visibility = View.VISIBLE
                },
                onError = {
                    Log.d("DetailFragment", "Error loading data: ${it.message}")
                    binding.pbDetail.visibility = View.VISIBLE
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.error))
                        .setMessage(resources.getString(R.string.failed_to_fetch_genres))
                        .setNeutralButton(resources.getString(R.string.close)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.try_again)) { dialog, which ->
                            uiAction(DetailAction.FetchProduct(productId))
                        }
                        .show()
                }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collect {
                if (it){
                    binding.btnFavorite.setImageResource(R.drawable.ic_filled_favorite)
                }else{
                    binding.btnFavorite.setImageResource(R.drawable.ic_unfilled_favorite)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect {
                when(it){
                    is DetailEffect.ShowMessageBar-> {
                        Snackbar.make(binding.root,it.message,Snackbar.LENGTH_SHORT).show()
                    }
                    is DetailEffect.BackButtonEffect->{
                        findNavController().navigateUp()
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            uiAction(DetailAction.BackButtonPressed)
        }
    }

    private fun getReview(reviews: List<Review>) {
        val adapter = ReviewAdapter()
        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        adapter.submitList(reviews)
    }

    private fun setData(data: DetailProduct) {
        binding.tvName.text = data.title
        Glide.with(binding.root)
            .load(data.thumbnail)
            .transform(RoundedCorners(20))
            .into(binding.imgProducts)
        val locale = Locale("us", "US")
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