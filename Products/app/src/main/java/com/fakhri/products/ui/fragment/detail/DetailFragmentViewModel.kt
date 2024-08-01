package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.BaseViewModel
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.AddFavoriteUseCase
import com.fakhri.products.domain.usecase.DeleteFavoriteUseCase
import com.fakhri.products.domain.usecase.GetDetailProductUseCase
import com.fakhri.products.domain.usecase.IsFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(
    private val getDetailProductUseCase: GetDetailProductUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : BaseViewModel<DetailState,DetailAction,DetailEffect>() {

    override val _state =  MutableStateFlow(DetailState())
    private var _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    private fun checkFavorite(id: Int): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            val load = isFavoriteUseCase(id)
            _isFavorite.value = load
        }
        return _isFavorite.value
    }

    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    override fun MutableSharedFlow<DetailAction>.updateStates() = onEach {
        when(it){
            is DetailAction.FetchProduct -> fetchMovie(it.id)
            is DetailAction.OnClickProduct -> onClickProduct(it.favoriteProductEntity)
            is DetailAction.BackButtonPressed-> processEffect(DetailEffect.BackButtonEffect)
        }
    }

    private fun onClickProduct(favoriteProductEntity: FavoriteProductEntity){
        viewModelScope.launch(Dispatchers.IO) {
            if (_isFavorite.value){
                deleteFavoriteUseCase(favoriteProductEntity)
                _isFavorite.value = false
                _state.update {
                    it.copy(
                        product = it.product,
                        isFavorite = it.isFavorite
                    )
                }
                processEffect(DetailEffect.ShowMessageBar("Product's remove from favorite"))
            }else{
                addFavoriteUseCase(favoriteProductEntity)
                _isFavorite.value = true
                _state.update {
                    it.copy(
                        product = it.product,
                        isFavorite = it.isFavorite
                    )
                }
                processEffect(DetailEffect.ShowMessageBar("Product's added to favorite"))
            }
        }
    }

    private fun fetchMovie(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getDetailProductUseCase(id).collect{
                _state.update {state->
                    state.copy(
                        product = it,
                       isFavorite = checkFavorite(id)
                    )
                }
            }
        }
    }

}

data class DetailState(
    val product: Resource<DetailProduct> = Resource.Idle(),
    val isFavorite: Boolean = false
)

sealed class DetailAction{
    data class FetchProduct(val id: Int): DetailAction()
    data class OnClickProduct(val favoriteProductEntity: FavoriteProductEntity): DetailAction()
    object BackButtonPressed: DetailAction()
}

sealed class DetailEffect{
    data class ShowMessageBar(val message: String): DetailEffect()
    object BackButtonEffect: DetailEffect()
}