package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.lifecycle.viewModelScope
import com.fakhri.products.ui.BaseViewModel
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.ChangeUserUseCase
import com.fakhri.products.domain.usecase.GetUserFromDBUseCase
import com.fakhri.products.domain.usecase.ResetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserFromDBUseCase: GetUserFromDBUseCase,
    private val changeUserUseCase: ChangeUserUseCase,
    private val resetUserUseCase: ResetUserUseCase
): BaseViewModel<EditProfileUIState, EditProfileUIAction, EditProfileUIEffect>() {

    override val _state = MutableStateFlow(EditProfileUIState())

    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    override fun MutableSharedFlow<EditProfileUIAction>.updateStates() = onEach {
        when(it){
            is EditProfileUIAction.FetchUser-> fetchUser(it.id)
            is EditProfileUIAction.ChangeUser-> changeUser(it.users)
            is EditProfileUIAction.ResetUser-> resetUser(it.id)
            is EditProfileUIAction.BackButtonPress-> processEffect(EditProfileUIEffect.BackToProfile)
            is EditProfileUIAction.ShowGalleryButtonPressed-> processEffect(EditProfileUIEffect.ShowGallery)
            is EditProfileUIAction.ShowDatePickerButtonPressed-> processEffect(EditProfileUIEffect.ShowDatePicker)
            is EditProfileUIAction.ShowCameraButtonPressed-> processEffect(EditProfileUIEffect.ShowCamera)
        }
    }

    private fun fetchUser(id: Int){
        viewModelScope.launch {
            getUserFromDBUseCase(id).collect{
                _state.update {
                    state->
                    state.copy(
                        user = it
                    )
                }
            }
        }
    }

    private fun changeUser(users: UsersEntity){
        viewModelScope.launch {
            changeUserUseCase(users).collect{resource->
                _state.update { state->
                    state.copy(
                        user = resource,
                        change = resource
                    )
                }
            }
            processEffect(EditProfileUIEffect.BackToProfile)
        }
    }
    private fun resetUser(id: Int){
        viewModelScope.launch {
            resetUserUseCase(id).collect{resource->
                _state.update {
                        state->
                    state.copy(
                        reset = resource
                    )
                }
            }
            processEffect(EditProfileUIEffect.BackToProfile)
        }
    }
}

sealed class EditProfileUIAction{
    data class ChangeUser(val users: UsersEntity): EditProfileUIAction()
    data class ResetUser(val id: Int): EditProfileUIAction()
    data class FetchUser(val id: Int): EditProfileUIAction()
    object BackButtonPress: EditProfileUIAction()
    object ShowGalleryButtonPressed: EditProfileUIAction()
    object ShowDatePickerButtonPressed: EditProfileUIAction()
    object ShowCameraButtonPressed: EditProfileUIAction()
}

data class EditProfileUIState(
    val user: Resource<UsersEntity> = Resource.Idle(),
    val change: Resource<UsersEntity> = Resource.Idle(),
    val reset: Resource<Unit> = Resource.Idle()
)

sealed class EditProfileUIEffect{
    object BackToProfile: EditProfileUIEffect()
    object ShowGallery: EditProfileUIEffect()
    object ShowDatePicker: EditProfileUIEffect()
    object ShowCamera: EditProfileUIEffect()
}