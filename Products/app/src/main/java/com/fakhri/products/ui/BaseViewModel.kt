package com.fakhri.products.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<StateType: Any, ActionType: Any, EffectType>: ViewModel() {

    protected abstract val _state: MutableStateFlow<StateType>
    val state: StateFlow<StateType>
        get() = _state.asStateFlow()

    open val _effect: MutableSharedFlow<EffectType> = MutableSharedFlow(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    open val effect get() = _effect.asSharedFlow()

    protected open val actionStateFlow = MutableSharedFlow<ActionType>(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * Process action
     *
     * @param action
     */
    fun processAction(action: ActionType) = actionStateFlow.tryEmit(action)

    /**
     * Process UI Effect
     *
     * @param effect
     */
    protected fun processEffect(effect: EffectType) = _effect.tryEmit(effect)
    /**
     * Update states
     *
     * @return
     */
    protected abstract fun MutableSharedFlow<ActionType>.updateStates(): Flow<ActionType>
}