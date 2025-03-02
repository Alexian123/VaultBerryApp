package com.alexianhentiu.vaultberryapp.presentation.viewmodel.state

sealed class AccountState {
    data object Init : AccountState()
    data object Idle : AccountState()
    data object Loading : AccountState()
    data object Deleted : AccountState()
    data class ChangedPassword(val recoveryPassword: String) : AccountState()
    data class Error(val message: String) : AccountState()
}