package com.fakhri.products.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhri.products.data.network.response.all.Product
import kotlinx.coroutines.flow.MutableStateFlow
import com.fakhri.products.data.utils.Result
import com.fakhri.products.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private var _data = MutableStateFlow<Result<PagingData<Product>>>(Result.Loading)
    val data: StateFlow<Result<PagingData<Product>>> = _data

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _data.value = Result.Loading
            try {
                getProductsUseCase().cachedIn(viewModelScope).collect {
                    _data.value = Result.Success(it)
                }
            } catch (e: Exception) {
                _data.value = Result.Failure(e)
            }
        }
    }
}