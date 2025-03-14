package com.alexianhentiu.vaultberryapp.presentation.utils.states

import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo

sealed class VaultState {
    data object Loading : VaultState()
    data object Locked : VaultState()
    data object Unlocked: VaultState()
    data class Error(val info: ErrorInfo) : VaultState()
}