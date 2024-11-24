package com.alexianhentiu.vaultberryapp.presentation.ui.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val decryptedVaultKey: DecryptedVaultKey) : LoginState()
    data class Error(val message: String) : LoginState()
}