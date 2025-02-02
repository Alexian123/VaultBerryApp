package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey

sealed class LoginState {
    data object LoggedOut : LoginState()
    data object Loading : LoginState()
    data class LoggedIn(val decryptedKey: DecryptedKey) : LoginState()
    data class Error(val message: String) : LoginState()
}