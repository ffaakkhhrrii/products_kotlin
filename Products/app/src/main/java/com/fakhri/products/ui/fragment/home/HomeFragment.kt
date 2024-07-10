package com.fakhri.products.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.fakhri.products.MainActivity
import com.fakhri.products.R
import com.fakhri.products.data.ProductRepository
import com.fakhri.products.data.paging.ProductPagingAdapter
import com.fakhri.products.data.utils.Result
import com.fakhri.products.databinding.FragmentHomeBinding
import com.fakhri.products.network.ApiConfig
import com.fakhri.products.ui.fragment.detail.DetailFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var adapter: ProductPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = ProductRepository(ApiConfig.instance)
        val factory = HomeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeFragmentViewModel::class.java)
        setUpRecycler()
    }

    private fun setUpRecycler() {
        adapter = ProductPagingAdapter{
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }

        binding.rvProducts.adapter = adapter

        val loadingState = MutableLiveData<Boolean>()

        adapter.addLoadStateListener { loadState ->
            loadingState.value = loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading
        }

        loadingState.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.data.collectLatest {
                result->
                when(result){
                    is Result.Loading ->{
                        Log.d("HomeFragment", "Loading data...")
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        Log.d("HomeFragment", "Data received ${result.data}")
                        binding.progressBar.visibility = View.GONE
                        adapter.submitData(result.data)
                    }
                    is Result.Failure ->{
                        Log.d("HomeFragment", "Error loading data: ${result.exception.message}")
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(),result.exception.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}