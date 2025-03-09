package com.alexianhentiu.vaultberryapp.presentation.viewmodel.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey

sealed class LoginState {
    data object LoggedOut : LoginState()
    data object Loading : LoginState()
    data class Verify2FA(val email: String, val password: String) : LoginState()
    data class LoggedIn(val decryptedKey: DecryptedKey) : LoginState()
    data class Error(val message: String) : LoginState()
}