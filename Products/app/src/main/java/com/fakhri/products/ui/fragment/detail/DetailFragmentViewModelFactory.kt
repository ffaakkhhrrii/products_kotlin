package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.repository.product.IProductRepository

class DetailFragmentViewModelFactory(private val repos: IProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailFragmentViewModel(repos) as T
    }
}