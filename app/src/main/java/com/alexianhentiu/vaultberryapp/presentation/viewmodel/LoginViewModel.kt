package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.Verify2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val verify2FAUseCase: Verify2FAUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val loginState: StateFlow<LoginState> = _loginState

    fun verify2FA(email: String, password: String, code: String) {
        val loginCredentials = LoginCredentials(email, password, code)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000) // Simulate network delay
            when (val result = verify2FAUseCase(loginCredentials)) {
                is ActionResult.Success -> {
                    _loginState.value = LoginState.LoggedIn(result.data)
                }

                is ActionResult.Error -> {
                    _loginState.value = LoginState.Error(
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

    fun login(email: String, password: String) {
        val loginCredentials = LoginCredentials(email, password)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000) // Simulate network delay
            when (val result = loginUseCase(loginCredentials)) {
                is ActionResult.Success -> {
                    _loginState.value = LoginState.LoggedIn(result.data)
                }

                is ActionResult.Error -> {  // Handle 2FA required case
                    if (result.message == "2FA required") {
                        _loginState.value = LoginState.Verify2FA(email, password)
                    } else {
                        _loginState.value = LoginState.Error(
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
    }

    fun resetState() {
        _loginState.value = LoginState.LoggedOut
    }
}