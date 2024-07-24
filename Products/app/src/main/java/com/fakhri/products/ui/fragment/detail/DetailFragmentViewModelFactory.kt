package com.fakhri.products.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.domain.usecase.AddFavoriteUseCase
import com.fakhri.products.domain.usecase.DeleteFavoriteUseCase
import com.fakhri.products.domain.usecase.GetDetailProductUseCase
import com.fakhri.products.domain.usecase.IsFavoriteUseCase

class DetailFragmentViewModelFactory(
    private val getDetailProductUseCase: GetDetailProductUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailFragmentViewModel(
            getDetailProductUseCase, isFavoriteUseCase, addFavoriteUseCase, deleteFavoriteUseCase
        ) as T
    }
}