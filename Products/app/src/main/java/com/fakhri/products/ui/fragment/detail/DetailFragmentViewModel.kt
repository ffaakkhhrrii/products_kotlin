package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.ProductRepository
import com.fakhri.products.data.model.detail.GetProductIdResponse
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailFragmentViewModel(private val repos: ProductRepository) : ViewModel() {
    private var _detailData = MutableStateFlow<Result<GetProductIdResponse>>(Result.Loading)
    val detailData: StateFlow<Result<GetProductIdResponse>> = _detailData

    fun getDetailData(id: Int) {
        viewModelScope.launch {
            _detailData.value = Result.Loading
            try {
                repos.getDataById(id).collect {
                    _detailData.value = it
                }
            } catch (e: Exception) {
                _detailData.value = Result.Failure(e)
            }
        }
    }

}