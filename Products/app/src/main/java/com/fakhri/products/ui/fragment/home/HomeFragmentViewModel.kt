package com.fakhri.products.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhri.products.data.ProductRepository
import com.fakhri.products.data.model.all.Product
import kotlinx.coroutines.flow.MutableStateFlow
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val repos: ProductRepository) : ViewModel() {
    private var _data = MutableStateFlow<Result<PagingData<Product>>>(Result.Loading)
    val data: StateFlow<Result<PagingData<Product>>> = _data

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            _data.value = Result.Loading
            try {
                repos.getData().cachedIn(viewModelScope).collect {
                    _data.value = Result.Success(it)
                }
            } catch (e: Exception) {
                _data.value = Result.Failure(e)
            }
        }
    }
}