package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey

sealed class AuthState {
    data object LoggedOut : AuthState()
    data object Loading : AuthState()
    data class LoggedIn(val decryptedKey: DecryptedKey) : AuthState()
    data class Error(val message: String) : AuthState()
}