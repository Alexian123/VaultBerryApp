package com.alexianhentiu.vaultberryapp.presentation.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerScreenState =
        MutableStateFlow<RegisterScreenState>(RegisterScreenState.Idle)
    val registerScreenState: StateFlow<RegisterScreenState> = _registerScreenState

    private val _recoveryPasswordEvent = Channel<String>()
    val recoveryPasswordEvent = _recoveryPasswordEvent.receiveAsFlow()

    fun register(
        email: String,
        password: String,
        firstName: String?,
        lastName: String?,
        noActivation: Boolean
    ) {
        val accountInfo = AccountInfo(
            email = email,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            _registerScreenState.value = RegisterScreenState.Loading
            when (val result = registerUseCase(accountInfo, password, noActivation)) {
                is UseCaseResult.Success -> {
                    _registerScreenState.value = RegisterScreenState.Success
                    _recoveryPasswordEvent.send(result.data)
                }

                is UseCaseResult.Error -> {
                    _registerScreenState.value = RegisterScreenState.Error(result.info)
                }
            }
        }
    }

    fun resetState() {
        _registerScreenState.value = RegisterScreenState.Idle
    }
}