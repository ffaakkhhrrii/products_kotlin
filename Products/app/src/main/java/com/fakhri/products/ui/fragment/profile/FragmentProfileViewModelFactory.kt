package com.fakhri.products.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.repository.product.IProductRepository
import com.fakhri.products.repository.user.IUserRepository

class FragmentProfileViewModelFactory(private val repository: IUserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentProfileViewModel(repository) as T
    }
}