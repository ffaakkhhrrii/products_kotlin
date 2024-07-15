package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.utils.Result
import com.fakhri.products.repository.user.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FragmentEditProfileViewModel(private val repos: IUserRepository): ViewModel() {
    private var _user = MutableStateFlow<Result<Users>>(Result.Loading)
    val user = _user

    fun showData(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _user.value = Result.Loading
            try {
                repos.getUserFromDB(id).collect{
                    _user.value = it
                }
            }catch (e: Exception){
                _user.value = Result.Failure(e)
            }
        }
    }

    fun addUser(users: Users){
        viewModelScope.launch(Dispatchers.IO) {
            repos.addUser(users)
        }
    }

    fun resetUser(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repos.resetUser(id)
        }
    }
}