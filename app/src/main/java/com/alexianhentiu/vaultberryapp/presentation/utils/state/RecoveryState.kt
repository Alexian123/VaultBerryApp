package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class RecoveryState {
    data object Idle : RecoveryState()

    data object Loading : RecoveryState()

    data class OTPRequested(val email: String) : RecoveryState()

    data class PasswordReset(val newRecoveryPassword: String) : RecoveryState()

    data class LoggedIn(val decryptedKey: ByteArray) : RecoveryState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LoggedIn

            return decryptedKey.contentEquals(other.decryptedKey)
        }

        override fun hashCode(): Int {
            return decryptedKey.contentHashCode()
        }
    }

    data class Error(val info: ErrorInfo) : RecoveryState()
}