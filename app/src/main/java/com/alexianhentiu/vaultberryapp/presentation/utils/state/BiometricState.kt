package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo

sealed class BiometricState {
    data object Idle : BiometricState()
    data object Loading : BiometricState()
    data object Authenticated : BiometricState()
    data object CredentialsStored : BiometricState()
    data class Error(val info: ErrorInfo) : BiometricState()
}
