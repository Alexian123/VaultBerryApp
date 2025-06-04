package com.alexianhentiu.vaultberryapp.presentation.ui.screens.account

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class AccountScreenState {
    data object Init : AccountScreenState()
    data object Loading : AccountScreenState()
    data object Idle : AccountScreenState()
    data object Setup2FA : AccountScreenState()
    data object ChangedPassword : AccountScreenState()
    data class Error(val info: ErrorInfo) : AccountScreenState()
}