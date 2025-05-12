package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class VaultState {
    data object Loading : VaultState()
    data object Locked : VaultState()
    data object Unlocked: VaultState()
    data class Error(val info: ErrorInfo) : VaultState()
}