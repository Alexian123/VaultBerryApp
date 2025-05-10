package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo

sealed class LoginState {
    data object LoggedOut : LoginState()
    data object Loading : LoginState()
    data class Verify2FA(val email: String, val password: String) : LoginState()
    data class LoggedIn(val decryptedKey: DecryptedKey) : LoginState()
    data class Error(val info: ErrorInfo) : LoginState()
}