package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val recoveryPassword: String) : RegisterState()
    data class Error(val info: ErrorInfo) : RegisterState()
}