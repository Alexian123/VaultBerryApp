package com.alexianhentiu.vaultberryapp.presentation.ui.screens.autofill

import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo

sealed class AutofillState {
    data object Idle : AutofillState()
    data object Loading : AutofillState()
    data object Success : AutofillState()
    data class Error(val info: ErrorInfo) : AutofillState()
}