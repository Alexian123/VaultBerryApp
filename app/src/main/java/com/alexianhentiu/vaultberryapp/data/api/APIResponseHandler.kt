package com.alexianhentiu.vaultberryapp.data.api

import org.json.JSONException
import org.json.JSONObject
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
                val errorBody = response.errorBody()?.string()
                try {
                    val jsonObject = errorBody?.let { JSONObject(it) } // Parse as JSON
                    val errorMessage = jsonObject?.getString("error") ?: "Unknown error"
                    APIResult.Error(errorMessage)
                } catch (e: JSONException) {
                    APIResult.Error(errorBody ?: "Unknown error") // Fallback if not JSON
                }
            }
        } catch (e: Exception) {
            APIResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}