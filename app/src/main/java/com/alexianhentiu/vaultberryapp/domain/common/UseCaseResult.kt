package com.alexianhentiu.vaultberryapp.domain.common

import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(
        val type: ErrorType,
        val source: String,
        val message: String
    ) : UseCaseResult<Nothing>()
}