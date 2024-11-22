package com.alexianhentiu.vaultberryapp.presentation.ui.login

import com.alexianhentiu.vaultberryapp.domain.model.LoginResponse

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val loginResponse: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}