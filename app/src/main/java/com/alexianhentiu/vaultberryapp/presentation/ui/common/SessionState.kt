package com.alexianhentiu.vaultberryapp.presentation.ui.common

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class SessionState {
    data object LoggedOut : SessionState()
    data object TwoFactorRequired : SessionState()
    data object ActivationEmailSent : SessionState()
    data object LoggedIn : SessionState()
    data object Loading : SessionState()
    data class Error(val info: ErrorInfo) : SessionState()
}