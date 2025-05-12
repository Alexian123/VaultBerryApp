package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class LoginState {
    data object LoggedOut : LoginState()

    data object Loading : LoginState()

    data class Verify2FA(val email: String, val password: String) : LoginState()

    data class LoggedIn(val decryptedKey: ByteArray) : LoginState() {
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

    data class Error(val info: ErrorInfo) : LoginState()
}