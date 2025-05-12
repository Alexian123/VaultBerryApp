package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class AccountState {
    data object Init : AccountState()
    data object Loading : AccountState()
    data object LoggedOut : AccountState()
    data class Idle(val accountInfo: AccountInfo, val is2FAEnabled: Boolean) : AccountState()
    data class Setup2FA(val secretKey: String) : AccountState()
    data class ChangedPassword(val recoveryPassword: String) : AccountState()
    data class Error(val info: ErrorInfo) : AccountState()
}