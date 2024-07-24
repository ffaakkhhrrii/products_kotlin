package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Result
import com.fakhri.products.domain.usecase.AddFavoriteUseCase
import com.fakhri.products.domain.usecase.DeleteFavoriteUseCase
import com.fakhri.products.domain.usecase.GetDetailProductUseCase
import com.fakhri.products.domain.usecase.IsFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailFragmentViewModel(
    private val getDetailProductUseCase: GetDetailProductUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {
    private var _detailData = MutableStateFlow<Result<DetailProduct>>(Result.Loading)
    val detailData: StateFlow<Result<DetailProduct>> = _detailData

    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun checkFavorite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavoriteUseCase(id)){
                _isFavorite.postValue(true)
            }else{
                _isFavorite.postValue(false)
            }
        }
    }

    fun toggleFavorite(favoriteProductEntity: FavoriteProductEntity){
        viewModelScope.launch(Dispatchers.IO) {
            if (_isFavorite.value == true){
                deleteFavoriteUseCase(favoriteProductEntity)
                _isFavorite.postValue(false)
            }else{
                addFavoriteUseCase(favoriteProductEntity)
                _isFavorite.postValue(true)
            }
        }
    }

    fun getDetailData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailData.value = Result.Loading
            try {
                getDetailProductUseCase(id).collect {
                    _detailData.value = it
                }
            } catch (e: Exception) {
                _detailData.value = Result.Failure(e)
            }
        }
    }

}