package com.alexianhentiu.vaultberryapp.domain.utils

sealed class ActionResult<out T> {
    data class Success<out T>(val data: T) : ActionResult<T>()
    data class Error(val message: String) : ActionResult<Nothing>()
}