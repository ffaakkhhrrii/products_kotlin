package com.fakhri.products.ui.fragment.profile

import androidx.lifecycle.viewModelScope
import com.fakhri.products.ui.BaseViewModel
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): BaseViewModel<ProfileState, ProfileAction, ProfileEffect>() {

    override val _state =  MutableStateFlow(ProfileState())

    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    override fun MutableSharedFlow<ProfileAction>.updateStates() = onEach {
        when(it){
            is ProfileAction.FetchUser -> showUser(it.id)
            is ProfileAction.ButtonEditProfilePress-> processEffect(ProfileEffect.NavigateToEditProfile(it.userId))
        }
    }

    private fun showUser(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase(id).collect{
                _state.update {
                    state->
                    state.copy(
                        user = it
                    )
                }
            }
        }
    }
}

sealed class ProfileAction{
    data class FetchUser(val id: Int) : ProfileAction()
    data class ButtonEditProfilePress(val userId: Int): ProfileAction()
}

data class ProfileState(
    val user: Resource<UsersEntity> = Resource.Idle()
)

sealed class ProfileEffect{
    data class NavigateToEditProfile(val userId: Int): ProfileEffect()
}