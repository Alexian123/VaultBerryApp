package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo

sealed class RegisterScreenState {
    data object Idle : RegisterScreenState()
    data object Loading : RegisterScreenState()
    data class Success(val recoveryPassword: String) : RegisterScreenState()
    data class Error(val info: ErrorInfo) : RegisterScreenState()
}