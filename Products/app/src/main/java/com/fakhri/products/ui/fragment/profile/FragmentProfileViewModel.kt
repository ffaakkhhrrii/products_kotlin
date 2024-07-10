package com.fakhri.products.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhri.products.data.ProductRepository
import com.fakhri.products.data.model.user.GetUserResponse
import com.fakhri.products.data.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FragmentProfileViewModel(private val repos: ProductRepository): ViewModel() {
    private var _dataUser = MutableStateFlow<Result<GetUserResponse>>(Result.Loading)
    val dataUser = _dataUser

    fun getUser(id: Int){
        viewModelScope.launch {
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