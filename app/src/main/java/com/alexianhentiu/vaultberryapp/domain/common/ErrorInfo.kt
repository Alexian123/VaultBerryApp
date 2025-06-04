package com.alexianhentiu.vaultberryapp.domain.common

import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType

data class ErrorInfo(
    val type: ErrorType = ErrorType.UNKNOWN,
    val source: String,
    val message: String
)