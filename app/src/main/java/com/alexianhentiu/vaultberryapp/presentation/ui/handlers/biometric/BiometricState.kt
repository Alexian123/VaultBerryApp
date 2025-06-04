package com.alexianhentiu.vaultberryapp.presentation.ui.handlers.biometric

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class BiometricState {
    object Idle : BiometricState()
    object Loading : BiometricState()
    object CredentialsStored : BiometricState()
    object Authenticated : BiometricState()
    object ClearedCredentials : BiometricState()
    data class Error(val info: ErrorInfo) : BiometricState()
}