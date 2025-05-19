package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class RecoveryScreenState {
    data object Idle : RecoveryScreenState()
    data object Loading : RecoveryScreenState()
    data object OTPRequested : RecoveryScreenState()
    data object PasswordReset : RecoveryScreenState()
    data object LoggedIn : RecoveryScreenState()
    data class Error(val info: ErrorInfo) : RecoveryScreenState()
}