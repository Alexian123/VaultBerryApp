package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo

sealed class AccountScreenState {
    data object Init : AccountScreenState()
    data object Loading : AccountScreenState()
    data object Idle : AccountScreenState()
    data object Setup2FA : AccountScreenState()
    data object ChangedPassword : AccountScreenState()
    data class Error(val info: ErrorInfo) : AccountScreenState()
}