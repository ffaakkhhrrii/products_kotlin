package com.fakhri.products.ui.fragment.favorite

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
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.databinding.FragmentFavoriteBinding
import com.fakhri.products.data.utils.handleCollect
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentFavorite : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        val action = {action: FavoriteListUIAction-> viewModel.processAction(action)}

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect{
                when(it){
                    is FavoriteListUIEffect.NavigateToDetail->{
                        val move = FragmentFavoriteDirections.actionFragmentFavoriteToDetailFragment(it.id)
                        findNavController().navigate(move)
                    }
                    is FavoriteListUIEffect.BackButtonEffect->{
                        findNavController().navigateUp()
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            action(FavoriteListUIAction.BackButtonPress)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collect{
                setUpRecycler(
                    favorites = it,
                    onTryAgain = {
                        action(FavoriteListUIAction.FetchFavoriteList)
                    },
                    onClickProduct = {
                        action(FavoriteListUIAction.OnClickProduct(it))
                    }
                )
            }
        }

        return binding.root
    }

    private fun setUpRecycler(favorites: PagingData<FavoriteProductEntity>,onTryAgain:()-> Unit,onClickProduct:(Int)->Unit) {
        adapter = FavoriteAdapter{
            onClickProduct(it)
        }

        binding.rvFavorite.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            viewModel.loadingState.value = loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
            val isEmpty = adapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading && loadState.prepend.endOfPaginationReached
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error

            if (isEmpty){
                binding.emptyListText.visibility = View.VISIBLE
            }else{
                binding.emptyListText.visibility = View.GONE
            }

            errorState?.let {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("Failed to fetch Products ${it.error.message.toString()}")
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
            viewModel.loadingState.collectLatest {
                binding.pbFavorite.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.submitData(favorites)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}