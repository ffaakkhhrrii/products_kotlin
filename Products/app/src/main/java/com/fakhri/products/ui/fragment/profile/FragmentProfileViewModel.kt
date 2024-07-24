package com.fakhri.products.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.network.response.user.Users
import com.fakhri.products.data.utils.Result
import com.fakhri.products.domain.IUserRepository
import com.fakhri.products.domain.usecase.GetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FragmentProfileViewModel(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {
    private var _dataUser = MutableStateFlow<Result<UsersEntity>>(Result.Loading)
    val dataUser = _dataUser

    fun getUser(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _dataUser.value = Result.Loading
            try {
                getUserUseCase(id).collect {
                    _dataUser.value = it
                }
            }catch (e: Exception){
                _dataUser.value = Result.Failure(e)
            }
        }
    }
}