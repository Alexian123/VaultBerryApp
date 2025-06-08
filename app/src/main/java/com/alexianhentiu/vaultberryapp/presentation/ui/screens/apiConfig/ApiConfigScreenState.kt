package com.alexianhentiu.vaultberryapp.presentation.ui.screens.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class ApiConfigScreenState {
    data object Loading : ApiConfigScreenState()
    data object Idle : ApiConfigScreenState()
    data object Ready : ApiConfigScreenState()
    data object Success : ApiConfigScreenState()
    data class Error(val info: ErrorInfo) : ApiConfigScreenState()
}