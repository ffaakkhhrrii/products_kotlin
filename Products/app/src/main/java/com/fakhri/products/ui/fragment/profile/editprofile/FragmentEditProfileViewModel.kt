package com.fakhri.products.ui.fragment.profile.editprofile

import androidx.lifecycle.viewModelScope
import com.fakhri.products.BaseViewModel
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.utils.Resource
import com.fakhri.products.domain.usecase.ChangeUserUseCase
import com.fakhri.products.domain.usecase.GetUserFromDBUseCase
import com.fakhri.products.domain.usecase.ResetUserUseCase
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
class FragmentEditProfileViewModel @Inject constructor(
    private val getUserFromDBUseCase: GetUserFromDBUseCase,
    private val changeUserUseCase: ChangeUserUseCase,
    private val resetUserUseCase: ResetUserUseCase
): BaseViewModel<EditProfileUIState,EditProfileUIAction,EditProfileUIEffect>() {

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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            changeUserUseCase(users)
            _state.update {
                state->
                state.copy(
                    user = state.user
                )
            }
            processEffect(EditProfileUIEffect.BackToProfile)
        }
    }
    private fun resetUser(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            resetUserUseCase(id)
            _state.update {
                state->
                state.copy(
                    user = state.user
                )
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
    val user: Resource<UsersEntity> = Resource.Idle()
)

sealed class EditProfileUIEffect{
    object BackToProfile: EditProfileUIEffect()
    object ShowGallery: EditProfileUIEffect()
    object ShowDatePicker: EditProfileUIEffect()
    object ShowCamera: EditProfileUIEffect()
}