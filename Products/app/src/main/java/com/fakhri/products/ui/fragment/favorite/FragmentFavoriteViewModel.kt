package com.fakhri.products.ui.fragment.favorite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.fakhri.products.BaseViewModel
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.GetAllFavoriteUseCase
import com.fakhri.products.ui.fragment.detail.DetailEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentFavoriteViewModel @Inject constructor(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase
): BaseViewModel<FavoriteListUIState,FavoriteListUIAction,FavoriteListUIEffect>() {

    override val _state = MutableStateFlow(FavoriteListUIState())

    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    override fun MutableSharedFlow<FavoriteListUIAction>.updateStates() = onEach {
        when(it){
            is FavoriteListUIAction.FetchFavoriteList-> fetchFavoriteList()
            is FavoriteListUIAction.OnClickProduct-> processEffect(FavoriteListUIEffect.NavigateToDetail(it.id))
            is FavoriteListUIAction.BackButtonPress-> processEffect(FavoriteListUIEffect.BackButtonEffect)
        }
    }

    private fun fetchFavoriteList(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllFavoriteUseCase(viewModelScope).collect{
                _state.update {
                    state->
                    state.copy(
                        favorites = it
                    )
                }
            }
        }
    }
}

sealed class FavoriteListUIAction{
    object FetchFavoriteList: FavoriteListUIAction()
    data class OnClickProduct(val id: Int): FavoriteListUIAction()
    object BackButtonPress: FavoriteListUIAction()
}

data class FavoriteListUIState(
    val favorites: Resource<PagingData<FavoriteProductEntity>> = Resource.Idle()
)

sealed class FavoriteListUIEffect{
    data class NavigateToDetail(val id: Int): FavoriteListUIEffect()
    object BackButtonEffect: FavoriteListUIEffect()
}