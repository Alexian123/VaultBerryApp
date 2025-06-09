package com.alexianhentiu.vaultberryapp.presentation.ui.screens.account

import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo

sealed class AccountScreenState {
    data object Init : AccountScreenState()
    data object Loading : AccountScreenState()
    data object Idle : AccountScreenState()
    data object Setup2FA : AccountScreenState()
    data object Activated2FA : AccountScreenState()
    data object ChangedEmail : AccountScreenState()
    data object ChangedPassword : AccountScreenState()
    data object DeletedAccount : AccountScreenState()
    data class Error(val info: ErrorInfo) : AccountScreenState()
}