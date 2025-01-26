package com.alexianhentiu.vaultberryapp.data.api

sealed class APIResult<out T> {
    data class Success<out T>(val data: T, val code: Int? = null) : APIResult<T>()
    data class Error(val message: String, val code: Int? = null) : APIResult<Nothing>()
}