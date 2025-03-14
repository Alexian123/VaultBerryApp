package com.alexianhentiu.vaultberryapp.presentation.utils.states

import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo

sealed class AccountState {
    data object Init : AccountState()
    data object Loading : AccountState()
    data object LoggedOut : AccountState()
    data class Idle(val account: Account, val is2FAEnabled: Boolean) : AccountState()
    data class Setup2FA(val secretKey: String) : AccountState()
    data class ChangedPassword(val recoveryPassword: String) : AccountState()
    data class Error(val info: ErrorInfo) : AccountState()
}