package com.alexianhentiu.vaultberryapp.presentation.utils.states

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo

sealed class RecoveryState {
    data object Idle : RecoveryState()
    data object Loading : RecoveryState()
    data class OTPRequested(val email: String) : RecoveryState()
    data class PasswordReset(val newRecoveryPassword: String) : RecoveryState()
    data class LoggedIn(val decryptedKey: DecryptedKey) : RecoveryState()
    data class Error(val info: ErrorInfo) : RecoveryState()
}