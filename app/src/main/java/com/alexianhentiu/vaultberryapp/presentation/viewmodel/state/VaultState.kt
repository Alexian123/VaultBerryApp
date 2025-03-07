package com.alexianhentiu.vaultberryapp.presentation.viewmodel.state

sealed class VaultState {
    data object Loading : VaultState()
    data object Locked : VaultState()
    data object Unlocked: VaultState()
    data class Error(val message: String) : VaultState()
}