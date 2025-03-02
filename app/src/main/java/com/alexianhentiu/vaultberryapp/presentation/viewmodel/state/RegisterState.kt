package com.alexianhentiu.vaultberryapp.presentation.viewmodel.state

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val recoveryPassword: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}