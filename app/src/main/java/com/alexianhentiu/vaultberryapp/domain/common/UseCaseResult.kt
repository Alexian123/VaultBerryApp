package com.alexianhentiu.vaultberryapp.domain.common

import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(val info: ErrorInfo) : UseCaseResult<Nothing>()
}