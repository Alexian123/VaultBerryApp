package com.alexianhentiu.vaultberryapp.presentation.viewmodel.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey

sealed class RecoveryState {
    data object Idle : RecoveryState()
    data object Loading : RecoveryState()
    data class OTPRequested(val email: String) : RecoveryState()
    data class PasswordReset(val newRecoveryPassword: String) : RecoveryState()
    data class LoggedIn(val decryptedKey: DecryptedKey) : RecoveryState()
    data class Error(val message: String) : RecoveryState()
}