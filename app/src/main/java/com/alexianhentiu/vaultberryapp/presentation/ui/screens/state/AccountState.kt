package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey

sealed class AccountState {
    data object Init : AccountState()
    data object Idle : AccountState()
    data object Loading : AccountState()
    data object Updated : AccountState()
    data object Deleted : AccountState()
    data object ForcedPasswordReset : AccountState()
    data class UpdatedPassword(val decryptedKey: DecryptedKey) : AccountState()
    data class Error(val message: String) : AccountState()
}