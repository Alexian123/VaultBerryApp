package com.alexianhentiu.vaultberryapp.data.remote

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T, val code: Int? = null) : ApiResult<T>()
    data class Error(
        val source: String,
        val message: String,
        val code: Int? = null
    ) : ApiResult<Nothing>()
}