package com.fakhri.products.ui.fragment.home

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhri.products.ui.BaseViewModel
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import com.fakhri.products.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel<Nothing, HomeAction,HomeEffect>() {

    val loadingState = MutableStateFlow(false)
    private var _pagingDataFlow: Flow<PagingData<Product>>
    val pagingDataFlow: Flow<PagingData<Product>> get() = _pagingDataFlow

    override val _state: MutableStateFlow<Nothing>
        get() = TODO("Not yet implemented")

    init {
        val products = actionStateFlow
            .filterIsInstance<HomeAction.FetchProducts>()
            .onStart {
                emit(HomeAction.FetchProducts)
            }

        _pagingDataFlow = products.flatMapLatest{
            fetchProducts()
        }.cachedIn(viewModelScope)

        actionStateFlow.updateStates().launchIn(viewModelScope)
        processAction(HomeAction.FetchProducts)
    }

    override fun MutableSharedFlow<HomeAction>.updateStates() = onEach {
        when (it) {
            is HomeAction.FetchProducts -> {
                fetchProducts()
            }
            is HomeAction.OnClickProduct -> processEffect(HomeEffect.NavigateToDetail(it.id))
            is HomeAction.OnClickButtonFavorite-> processEffect(HomeEffect.NavigateToFavorite)
        }
    }

    private fun fetchProducts(): Flow<PagingData<Product>>{
        return getProductsUseCase()
    }
}

sealed class HomeAction {
    object FetchProducts : HomeAction()
    data class OnClickProduct(val id: Int): HomeAction()
    object OnClickButtonFavorite: HomeAction()
}

sealed class HomeEffect{
    data class NavigateToDetail(val id: Int): HomeEffect()
    object NavigateToFavorite: HomeEffect()
}