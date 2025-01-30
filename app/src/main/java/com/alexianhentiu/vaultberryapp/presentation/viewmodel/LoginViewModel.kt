package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        val loginCredentials = LoginCredentials(email, password)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000) // Simulate network delay
            when (val result = loginUseCase(loginCredentials)) {
                is APIResult.Success -> {
                    _loginState.value = LoginState.LoggedIn(result.data)
                }

                is APIResult.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                    Log.e("LoginViewModel", "Login failed: ${result.message}")
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.LoggedOut
    }
}