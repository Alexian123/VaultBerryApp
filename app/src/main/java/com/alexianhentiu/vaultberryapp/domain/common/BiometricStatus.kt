package com.alexianhentiu.vaultberryapp.domain.common

sealed class BiometricStatus {
    object Available : BiometricStatus()
    class Error(val info: ErrorInfo) : BiometricStatus()
}