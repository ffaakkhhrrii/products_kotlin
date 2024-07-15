package com.fakhri.products.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.utils.Result
import com.fakhri.products.repository.product.IProductRepository
import com.fakhri.products.repository.user.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FragmentProfileViewModel(private val repos: IUserRepository): ViewModel() {
    private var _dataUser = MutableStateFlow<Result<Users>>(Result.Loading)
    val dataUser = _dataUser

    fun getUser(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _dataUser.value = Result.Loading
            try {
                repos.getUser(id).collect {
                    _dataUser.value = it
                }
            }catch (e: Exception){
                _dataUser.value = Result.Failure(e)
            }
        }
    }
}