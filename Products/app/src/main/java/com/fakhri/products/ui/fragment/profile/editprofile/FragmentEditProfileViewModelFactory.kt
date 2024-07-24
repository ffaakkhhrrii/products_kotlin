package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.domain.IUserRepository
import com.fakhri.products.domain.usecase.ChangeUserUseCase
import com.fakhri.products.domain.usecase.GetUserFromDBUseCase
import com.fakhri.products.domain.usecase.ResetUserUseCase

class FragmentEditProfileViewModelFactory(
    private val getUserFromDBUseCase: GetUserFromDBUseCase,
    private val changeUserUseCase: ChangeUserUseCase,
    private val resetUserUseCase: ResetUserUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentEditProfileViewModel(
            getUserFromDBUseCase, changeUserUseCase, resetUserUseCase
        ) as T
    }
}