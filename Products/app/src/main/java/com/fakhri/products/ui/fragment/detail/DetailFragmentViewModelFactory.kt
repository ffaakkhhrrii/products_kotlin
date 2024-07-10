package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.data.ProductRepository

class DetailFragmentViewModelFactory(private val repos: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailFragmentViewModel(repos) as T
    }
}