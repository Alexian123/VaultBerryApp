package com.alexianhentiu.vaultberryapp.presentation.utils

import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

data class ErrorInfo(
    val type: ErrorType = ErrorType.UNKNOWN,
    val source: String,
    val message: String,
    val critical: Boolean = false,
    val shouldRetry: Boolean = false
)