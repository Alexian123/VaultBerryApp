package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RegisterScreenState
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

    private val _registerScreenState = MutableStateFlow<RegisterScreenState>(RegisterScreenState.Idle)
    val registerScreenState: StateFlow<RegisterScreenState> = _registerScreenState

    private val _recoveryPasswordEvent = Channel<String>()
    val recoveryPasswordEvent = _recoveryPasswordEvent.receiveAsFlow()

    fun register(email: String, password: String, firstName: String?, lastName: String?) {
        val accountInfo = AccountInfo(
            email = email,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            _registerScreenState.value = RegisterScreenState.Loading
            when (val result = registerUseCase(accountInfo, password)) {
                is UseCaseResult.Success -> {
                    _registerScreenState.value = RegisterScreenState.Success
                    _recoveryPasswordEvent.send(result.data)
                }

                is UseCaseResult.Error -> {
                    _registerScreenState.value = RegisterScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
        }
    }

    fun resetState() {
        _registerScreenState.value = RegisterScreenState.Idle
    }
}