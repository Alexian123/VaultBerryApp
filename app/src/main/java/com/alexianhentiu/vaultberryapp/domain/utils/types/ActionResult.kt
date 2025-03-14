package com.alexianhentiu.vaultberryapp.domain.utils.types

sealed class ActionResult<out T> {
    data class Success<out T>(val data: T) : ActionResult<T>()
    data class Error(
        val type: ErrorType,
        val source: String,
        val message: String
    ) : ActionResult<Nothing>()
}