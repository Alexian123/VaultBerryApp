package com.alexianhentiu.vaultberryapp.domain.utils.types

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(
        val type: ErrorType,
        val source: String,
        val message: String
    ) : UseCaseResult<Nothing>()
}