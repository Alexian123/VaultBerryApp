package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo

sealed class BiometricState {
    object Idle : BiometricState()
    object Loading : BiometricState()
    object CredentialsStored : BiometricState()
    object Authenticated : BiometricState()
    object ClearedCredentials : BiometricState()
    data class Error(val info: ErrorInfo) : BiometricState()
}
