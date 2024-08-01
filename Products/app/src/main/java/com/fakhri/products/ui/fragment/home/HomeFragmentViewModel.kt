package com.fakhri.products.ui.fragment.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.fakhri.products.BaseViewModel
import com.fakhri.products.data.network.response.all.Product
import com.fakhri.products.data.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import com.fakhri.products.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel<HomeState, HomeAction,HomeEffect>() {

    override val _state = MutableStateFlow(HomeState())

    init {
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

    private fun fetchProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase(viewModelScope).collect{
                _state.update {state->
                    state.copy(
                        products = it
                    )
                }
            }
        }
    }
}

sealed class HomeAction {
    object FetchProducts : HomeAction()
    data class OnClickProduct(val id: Int): HomeAction()
    object OnClickButtonFavorite: HomeAction()
}

data class HomeState(
    val products: Resource<PagingData<Product>> = Resource.Idle()
)

sealed class HomeEffect{
    data class NavigateToDetail(val id: Int): HomeEffect()
    object NavigateToFavorite: HomeEffect()
}