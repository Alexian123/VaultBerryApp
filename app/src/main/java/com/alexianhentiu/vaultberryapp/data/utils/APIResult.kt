package com.alexianhentiu.vaultberryapp.data.utils

sealed class APIResult<out T> {
    data class Success<out T>(val data: T, val code: Int? = null) : APIResult<T>()
    data class Error(
        val source: String,
        val message: String,
        val code: Int? = null
    ) : APIResult<Nothing>()
}