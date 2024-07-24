package com.fakhri.products.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.domain.IUserRepository
import com.fakhri.products.domain.usecase.GetUserUseCase

class FragmentProfileViewModelFactory(
    private val getUserUseCase: GetUserUseCase
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentProfileViewModel(getUserUseCase) as T
    }
}