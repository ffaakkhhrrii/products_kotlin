package com.fakhri.products.ui.fragment.profile.editprofile

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fakhri.products.repository.user.IUserRepository

class FragmentEditProfileViewModelFactory(private val repos: IUserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentEditProfileViewModel(repos) as T
    }
}