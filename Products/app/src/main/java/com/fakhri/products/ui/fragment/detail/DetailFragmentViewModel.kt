package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.local.db.product.FavoriteProduct
import com.fakhri.products.data.network.model.detail.DetailProduct
import com.fakhri.products.data.utils.Result
import com.fakhri.products.repository.product.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailFragmentViewModel(private val repos: IProductRepository) : ViewModel() {
    private var _detailData = MutableStateFlow<Result<DetailProduct>>(Result.Loading)
    val detailData: StateFlow<Result<DetailProduct>> = _detailData

    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun checkFavorite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val validation = repos.isFavorite(id)
            if (validation){
                _isFavorite.postValue(true)
            }else{
                _isFavorite.postValue(false)
            }
        }
    }

    fun toggleFavorite(favoriteProduct: FavoriteProduct){
        viewModelScope.launch(Dispatchers.IO) {
            if (_isFavorite.value == true){
                repos.unFavoriteProduct(favoriteProduct)
                _isFavorite.postValue(false)
            }else{
                repos.addToFavorite(favoriteProduct)
                _isFavorite.postValue(true)
            }
        }
    }

    fun getDetailData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailData.value = Result.Loading
            try {
                repos.getProduct(id).collect {
                    _detailData.value = it
                }
            } catch (e: Exception) {
                _detailData.value = Result.Failure(e)
            }
        }
    }

}