package com.fakhri.products.ui.fragment.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.data.repository.ProductRepository
import com.fakhri.products.domain.usecase.GetAllFavoriteUseCase

class FragmentFavoriteViewModelFactory(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentFavoriteViewModel(getAllFavoriteUseCase) as T
    }
}