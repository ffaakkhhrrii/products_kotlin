package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.network.response.user.Users
import com.fakhri.products.data.utils.Result
import com.fakhri.products.domain.IUserRepository
import com.fakhri.products.domain.usecase.ChangeUserUseCase
import com.fakhri.products.domain.usecase.GetUserFromDBUseCase
import com.fakhri.products.domain.usecase.ResetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FragmentEditProfileViewModel(
    private val getUserFromDBUseCase: GetUserFromDBUseCase,
    private val changeUserUseCase: ChangeUserUseCase,
    private val resetUserUseCase: ResetUserUseCase
): ViewModel() {
    private var _user = MutableStateFlow<Result<UsersEntity>>(Result.Loading)
    val user = _user

    fun showData(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _user.value = Result.Loading
            try {
                getUserFromDBUseCase(id).collect{
                    _user.value = it
                }
            }catch (e: Exception){
                _user.value = Result.Failure(e)
            }
        }
    }

    fun addUser(users: UsersEntity){
        viewModelScope.launch(Dispatchers.IO) {
            changeUserUseCase(users)
        }
    }

    fun resetUser(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            resetUserUseCase(id)
        }
    }
}