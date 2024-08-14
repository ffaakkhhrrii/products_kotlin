package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.viewModelScope
import com.fakhri.products.ui.BaseViewModel
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.response.detail.DetailProduct
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.AddFavoriteUseCase
import com.fakhri.products.domain.usecase.DeleteFavoriteUseCase
import com.fakhri.products.domain.usecase.GetDetailProductUseCase
import com.fakhri.products.domain.usecase.IsFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailProductUseCase: GetDetailProductUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : BaseViewModel<DetailState, DetailAction, DetailEffect>() {

    override val _state =  MutableStateFlow(DetailState())
    var _isFavorite = MutableStateFlow(false)

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
            is DetailAction.FetchProduct -> fetchDetailProduct(it.id)
            is DetailAction.OnClickProduct -> onClickProduct(it.favoriteProductEntity)
            is DetailAction.BackButtonPressed-> processEffect(DetailEffect.BackButtonEffect)
        }
    }

    private fun onClickProduct(favoriteProductEntity: FavoriteProductEntity){
        viewModelScope.launch(Dispatchers.IO) {
            if (_isFavorite.value){
                deleteFavoriteUseCase(favoriteProductEntity).collect{resource->
                    _isFavorite.value = false
                    _state.update {
                        it.copy(
                            isFavorite = false,
                            deleteFavorite = resource
                        )
                    }
                }
                processEffect(DetailEffect.ShowMessageBar("Product's remove from favorite"))
            }else{
                addFavoriteUseCase(favoriteProductEntity).collect{resource->
                    _isFavorite.value = true
                    _state.update {
                        it.copy(
                            isFavorite = true,
                            addFavorite = resource
                        )
                    }
                }
                processEffect(DetailEffect.ShowMessageBar("Product's added to favorite"))
            }
        }
    }

    private fun fetchDetailProduct(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getDetailProductUseCase(id).collect{
                _state.update {state->
                    state.copy(
                        product = it,
                       isFavorite = isFavoriteUseCase(id)
                    )
                }
            }
            checkFavorite(id)
        }
    }
}

data class DetailState(
    val product: Resource<DetailProduct> = Resource.Idle(),
    val isFavorite: Boolean = false,
    val addFavorite: Resource<FavoriteProductEntity> = Resource.Idle(),
    val deleteFavorite: Resource<FavoriteProductEntity> = Resource.Idle()
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