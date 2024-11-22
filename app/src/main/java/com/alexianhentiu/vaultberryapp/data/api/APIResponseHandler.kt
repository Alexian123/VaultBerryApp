package com.alexianhentiu.vaultberryapp.data.api

import android.util.Log
import retrofit2.Response

class APIResponseHandler {

    // Generic function to handle API calls and map responses
    suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> Response<T>,
        transform: (T) -> R
    ): APIResult<R> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    APIResult.Success(transform(body))
                } else {
                    APIResult.Error("Empty response body")
                }
            } else {
                APIResult.Error(response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            APIResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}