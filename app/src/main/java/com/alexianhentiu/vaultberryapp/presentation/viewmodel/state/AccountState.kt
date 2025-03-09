package com.alexianhentiu.vaultberryapp.presentation.viewmodel.state

import com.alexianhentiu.vaultberryapp.domain.model.Account

sealed class AccountState {
    data object Init : AccountState()
    data object Loading : AccountState()
    data object Deleted : AccountState()
    data class Idle(val account: Account, val is2FAEnabled: Boolean) : AccountState()
    data class Setup2FA(val secretKey: String) : AccountState()
    data class ChangedPassword(val recoveryPassword: String) : AccountState()
    data class Error(val message: String) : AccountState()
}