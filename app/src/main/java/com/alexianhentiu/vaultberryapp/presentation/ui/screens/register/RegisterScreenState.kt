package com.alexianhentiu.vaultberryapp.presentation.ui.screens.register

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class RegisterScreenState {
    data object Idle : RegisterScreenState()
    data object Loading : RegisterScreenState()
    data object Success : RegisterScreenState()
    data class Error(val info: ErrorInfo) : RegisterScreenState()
}