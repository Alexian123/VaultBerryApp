package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey

sealed class LoginState {
    data object LoggedOut : LoginState()
    data object Loading : LoginState()
    data class LoggedIn(val decryptedVaultKey: DecryptedVaultKey) : LoginState()
    data class Error(val message: String) : LoginState()
}