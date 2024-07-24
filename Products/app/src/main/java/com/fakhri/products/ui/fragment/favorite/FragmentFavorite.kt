package com.fakhri.products.ui.fragment.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.fakhri.products.data.utils.Result
import com.fakhri.products.databinding.FragmentFavoriteBinding
import com.fakhri.products.data.network.api.ApiConfig
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.domain.usecase.GetAllFavoriteUseCase
import kotlinx.coroutines.launch

class FragmentFavorite : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FragmentFavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = ProductRepository(ApiConfig.instance,requireContext())
        val getAllFavoriteUseCase = GetAllFavoriteUseCase(repository)
        val factory = FragmentFavoriteViewModelFactory(getAllFavoriteUseCase)
        viewModel = ViewModelProvider(this, factory).get(FragmentFavoriteViewModel::class.java)
        setUpRecycler()
    }

    private fun setUpRecycler() {
        adapter = FavoriteAdapter{
            val action = FragmentFavoriteDirections.actionFragmentFavoriteToDetailFragment(it)
            findNavController().navigate(action)
        }

        binding.rvFavorite.adapter = adapter

        val loadingState = MutableLiveData<Boolean>()

        adapter.addLoadStateListener { loadState ->
            loadingState.value = loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
        }

        loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.pbFavorite.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.data.collect {
                    result->
                when(result){
                    is Result.Loading ->{
                        Log.d("FragmenFavorite", "Loading data...")
                        binding.pbFavorite.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        Log.d("HomeFragment", "Data received ${result.data}")
                        binding.pbFavorite.visibility = View.GONE
                        adapter.submitData(result.data)
                    }
                    is Result.Failure ->{
                        Log.d("HomeFragment", "Error loading data: ${result.exception.message}")
                        binding.pbFavorite.visibility = View.GONE
                        Toast.makeText(requireActivity(),result.exception.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}