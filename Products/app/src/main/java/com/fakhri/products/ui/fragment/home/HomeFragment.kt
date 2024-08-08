package com.fakhri.products.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.fakhri.products.data.network.paging.ProductPagingAdapter
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.utils.handleCollect
import com.fakhri.products.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ProductPagingAdapter
    //val idlingResource = CountingIdlingResource("Paging")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val uiAction = { action: HomeAction -> viewModel.processAction(action) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect {
                when (it) {
                    is HomeEffect.NavigateToDetail -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.id)
                        findNavController().navigate(action)
                    }

                    is HomeEffect.NavigateToFavorite -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToFragmentFavorite()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        binding.btnToFavorite.setOnClickListener {
            uiAction(HomeAction.OnClickButtonFavorite)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collect {
                setUpRecycler(it,
                onTryAgain = {
                uiAction(HomeAction.FetchProducts)
                },
                onClickProduct = {
                uiAction(HomeAction.OnClickProduct(it))
                })
            }
        }

        return binding.root
    }

    private fun setUpRecycler(
        product: PagingData<Product>,
        onTryAgain: () -> Unit,
        onClickProduct: (Int) -> Unit
    ) {
        adapter = ProductPagingAdapter {
            onClickProduct(it)
        }

        binding.rvProducts.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            viewModel.loadingState.value =
                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            val isEmpty =
                adapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading && loadState.prepend.endOfPaginationReached

            if (isEmpty) {
                binding.emptyListTextProduct.visibility = View.VISIBLE
            } else {
                binding.emptyListTextProduct.visibility = View.GONE
            }
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
            viewModel.loadingState.collectLatest { isLoading ->
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
