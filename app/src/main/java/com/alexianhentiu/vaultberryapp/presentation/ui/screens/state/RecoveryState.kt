package com.alexianhentiu.vaultberryapp.presentation.ui.screens.state

sealed class RecoveryState {
    data object Idle : RecoveryState()
    data object Loading : RecoveryState()
    data object ResetPassword : RecoveryState()
    data object Success : RecoveryState()
    data class Error(val message: String) : RecoveryState()
}
