package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val loginState: StateFlow<LoginState> = _loginState

    fun verify2FA(email: String, password: String, code: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000) // Simulate network delay
            when (val result = loginUseCase(email = email, password = password, totpCode = code)) {
                is UseCaseResult.Success -> {
                    _loginState.value = LoginState.LoggedIn(result.data)
                }

                is UseCaseResult.Error -> {
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
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000) // Simulate network delay
            when (val result = loginUseCase(email = email, password = password)) {
                is UseCaseResult.Success -> {
                    _loginState.value = LoginState.LoggedIn(result.data)
                }

                is UseCaseResult.Error -> {  // Handle 2FA required case
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