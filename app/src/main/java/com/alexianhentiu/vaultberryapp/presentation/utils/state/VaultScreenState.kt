package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.store.ErrorInfo

sealed class VaultScreenState {
    data object Loading : VaultScreenState()
    data object Locked : VaultScreenState()
    data object Unlocked: VaultScreenState()
    data class Error(val info: ErrorInfo) : VaultScreenState()
}