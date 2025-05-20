package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.store.ErrorInfo

sealed class SessionState {
    data object LoggedOut : SessionState()
    data object TwoFactorRequired : SessionState()
    data object LoggedIn : SessionState()
    data object Loading : SessionState()
    data class Error(val info: ErrorInfo) : SessionState()
}
