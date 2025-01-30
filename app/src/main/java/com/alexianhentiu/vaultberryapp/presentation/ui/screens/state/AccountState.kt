package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

sealed class AccountState {
    data object Init : AccountState()
    data object Idle : AccountState()
    data object Loading : AccountState()
    data object UpdatedEmail : AccountState()
    data object UpdatedName : AccountState()
    data object UpdatedPassword : AccountState()
    data object Deleted : AccountState()
    data class Error(val message: String) : AccountState()
}