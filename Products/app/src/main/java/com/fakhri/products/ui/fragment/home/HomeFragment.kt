package com.fakhri.products.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import com.fakhri.products.data.network.paging.ProductPagingAdapter
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.utils.handleCollect
import com.fakhri.products.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var adapter: ProductPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val uiState = viewModel.state
        val uiAction = { action: HomeAction -> viewModel.processAction(action) }
        val productFlow = uiState.map { it.products }.distinctUntilChanged()
        viewLifecycleOwner.lifecycleScope.launch {
            productFlow.handleCollect(
                onSuccess = {result ->
                    binding.progressBar.visibility = View.GONE
                    setUpRecycler(result.data!!,
                        onTryAgain = {
                            uiAction(HomeAction.FetchProducts)
                        },
                        onClickProduct = {
                            uiAction(HomeAction.OnClickProduct(it))
                        })
                    Log.i("HomeFragment", "Load Data Success")
                },
                onLoading = {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.i("HomeFragment", "Loading data")
                },
                onError = { resource ->
                    binding.progressBar.visibility = View.GONE
                    Log.e("HomeFragment", "Error: ${resource}")
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage(resource.message ?: "Failed to fetch Products")
                        .setNeutralButton("Close") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Try Again") { _, _ ->
                            uiAction(HomeAction.FetchProducts)
                        }
                        .show()
                },
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect {
                when(it){
                    is HomeEffect.NavigateToDetail->{
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.id)
                        findNavController().navigate(action)
                    }
                    is HomeEffect.NavigateToFavorite->{
                        val action = HomeFragmentDirections.actionHomeFragmentToFragmentFavorite()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        binding.btnToFavorite.setOnClickListener {
            uiAction(HomeAction.OnClickButtonFavorite)
        }

        return binding.root
    }

    private fun setUpRecycler(product: PagingData<Product>,onTryAgain:()-> Unit,onClickProduct:(Int)-> Unit) {
        adapter = ProductPagingAdapter {
            onClickProduct(it)
        }

        binding.rvProducts.adapter = adapter

        val loadingState = MutableStateFlow(false)

        adapter.addLoadStateListener { loadState ->
            loadingState.value = loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error

            errorState?.let {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("Failed to fetch Products")
                    .setNeutralButton("Close") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Try Again") { _, _ ->
                        onTryAgain()
                    }
                    .show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loadingState.collectLatest { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.submitData(product)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
