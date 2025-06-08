package com.alexianhentiu.vaultberryapp.domain.common

import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo

sealed class BiometricStatus {
    object Available : BiometricStatus()
    class Error(val info: ErrorInfo) : BiometricStatus()
}