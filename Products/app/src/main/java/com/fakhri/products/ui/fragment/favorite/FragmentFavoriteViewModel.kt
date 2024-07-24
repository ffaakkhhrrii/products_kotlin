package com.fakhri.products.ui.fragment.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.utils.Result
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.domain.usecase.GetAllFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FragmentFavoriteViewModel(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase
):ViewModel() {
    private var _data = MutableStateFlow<Result<PagingData<FavoriteProductEntity>>>(Result.Loading)
    val data: StateFlow<Result<PagingData<FavoriteProductEntity>>> = _data

    init {
        getAllFavorite()
    }

    private fun getAllFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            _data.value = Result.Loading
            try {
                getAllFavoriteUseCase().cachedIn(viewModelScope).collect {
                    _data.value = Result.Success(it)
                }
            } catch (e: Exception) {
                _data.value = Result.Failure(e)
            }
        }
    }
}