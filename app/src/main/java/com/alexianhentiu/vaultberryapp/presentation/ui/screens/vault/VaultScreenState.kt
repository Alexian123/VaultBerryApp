package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo

sealed class VaultScreenState {
    data object Loading : VaultScreenState()
    data object Locked : VaultScreenState()
    data object Unlocked: VaultScreenState()
    data class Error(val info: ErrorInfo) : VaultScreenState()
}