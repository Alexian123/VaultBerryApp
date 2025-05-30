package com.alexianhentiu.vaultberryapp.presentation.utils.biometric

import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo

sealed class BiometricStatus {
    object Available : BiometricStatus()
    class Error(val info: ErrorInfo) : BiometricStatus()
}