package com.alexianhentiu.vaultberryapp.presentation.ui.state

sealed class VaultState {
    data object Idle : VaultState()
    data object Loading : VaultState()
    data object Success : VaultState()
    data class Error(val message: String) : VaultState()
}