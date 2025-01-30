package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

sealed class VaultState {
    data object Loading : VaultState()
    data object Locked : VaultState()
    data object Unlocked: VaultState()
    data class Error(val message: String) : VaultState()
}