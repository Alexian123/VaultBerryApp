package com.alexianhentiu.vaultberryapp.presentation.utils.states

import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val recoveryPassword: String) : RegisterState()
    data class Error(val info: ErrorInfo) : RegisterState()
}