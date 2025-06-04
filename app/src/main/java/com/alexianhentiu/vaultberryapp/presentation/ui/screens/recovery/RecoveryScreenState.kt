package com.alexianhentiu.vaultberryapp.presentation.ui.screens.recovery

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class RecoveryScreenState {
    data object Idle : RecoveryScreenState()
    data object Loading : RecoveryScreenState()
    data object OTPRequested : RecoveryScreenState()
    data object PasswordReset : RecoveryScreenState()
    data object LoggedIn : RecoveryScreenState()
    data class Error(val info: ErrorInfo) : RecoveryScreenState()
}