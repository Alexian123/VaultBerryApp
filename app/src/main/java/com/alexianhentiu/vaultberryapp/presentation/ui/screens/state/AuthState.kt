package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey

sealed class AuthState {
    data object LoggedOut : AuthState()
    data object Loading : AuthState()
    data class LoggedIn(val decryptedVaultKey: DecryptedVaultKey) : AuthState()
    data class Error(val message: String) : AuthState()
}