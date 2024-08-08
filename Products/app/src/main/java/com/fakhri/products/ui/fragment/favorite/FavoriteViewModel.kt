package com.fakhri.products.ui.fragment.favorite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhri.products.ui.BaseViewModel
import com.fakhri.products.data.local.db.product.FavoriteProductEntity
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.GetAllFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase
): BaseViewModel<Nothing, FavoriteListUIAction, FavoriteListUIEffect>() {

    val loadingState = MutableStateFlow(false)
    private var _pagingDataFlow: Flow<PagingData<FavoriteProductEntity>>
    val pagingDataFlow: Flow<PagingData<FavoriteProductEntity>> get() = _pagingDataFlow
    override val _state: MutableStateFlow<Nothing>
        get() = TODO("Not yet implemented")

    init {
        val favorites = actionStateFlow.filterIsInstance<FavoriteListUIAction.FetchFavoriteList>()
            .onStart {
                emit(FavoriteListUIAction.FetchFavoriteList)
            }
        _pagingDataFlow = favorites.flatMapLatest {
            fetchFavoriteList()
        }.cachedIn(viewModelScope)
        actionStateFlow.updateStates().launchIn(viewModelScope)
        processAction(FavoriteListUIAction.FetchFavoriteList)
    }

    override fun MutableSharedFlow<FavoriteListUIAction>.updateStates() = onEach {
        when(it){
            is FavoriteListUIAction.FetchFavoriteList-> fetchFavoriteList()
            is FavoriteListUIAction.OnClickProduct-> processEffect(FavoriteListUIEffect.NavigateToDetail(it.id))
            is FavoriteListUIAction.BackButtonPress-> processEffect(FavoriteListUIEffect.BackButtonEffect)
        }
    }

    private fun fetchFavoriteList(): Flow<PagingData<FavoriteProductEntity>>{
        return getAllFavoriteUseCase()
    }
}

sealed class FavoriteListUIAction{
    object FetchFavoriteList: FavoriteListUIAction()
    data class OnClickProduct(val id: Int): FavoriteListUIAction()
    object BackButtonPress: FavoriteListUIAction()
}

sealed class FavoriteListUIEffect{
    data class NavigateToDetail(val id: Int): FavoriteListUIEffect()
    object BackButtonEffect: FavoriteListUIEffect()
}