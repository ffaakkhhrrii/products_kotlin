package com.fakhri.products.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.domain.usecase.GetProductsUseCase

class HomeFragmentViewModelFactory(private val getProductsUseCase: GetProductsUseCase):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeFragmentViewModel(getProductsUseCase) as T
    }
}