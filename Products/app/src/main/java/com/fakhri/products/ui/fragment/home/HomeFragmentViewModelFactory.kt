package com.fakhri.products.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.repository.product.IProductRepository

class HomeFragmentViewModelFactory(private val repos: IProductRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeFragmentViewModel(repos) as T
    }
}