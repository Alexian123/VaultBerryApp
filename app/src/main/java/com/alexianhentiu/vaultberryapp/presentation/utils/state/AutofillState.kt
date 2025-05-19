package com.alexianhentiu.vaultberryapp.presentation.utils.state

import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo

sealed class AutofillState {
    data object Idle : AutofillState()
    data object Loading : AutofillState()
    data object Success : AutofillState()
    data class Error(val info: ErrorInfo) : AutofillState()
}
