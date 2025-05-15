package com.alexianhentiu.vaultberryapp.data.utils

import android.util.Log
import okio.ProtocolException
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
                    APIResult.Error(
                        BACKEND_ERROR_SOURCE,
                        "Empty response body",
                        response.code()
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                try {
                    val jsonObject = errorBody?.let { JSONObject(it) } // Parse as JSON
                    val errorMessage = jsonObject?.getString("error") ?: "Unknown error"
                    APIResult.Error(BACKEND_ERROR_SOURCE, errorMessage, response.code())
                } catch (e: JSONException) {
                    APIResult.Error(
                        JSON_ERROR_SOURCE,
                        errorBody ?: e.message ?: "An unexpected error occurred",
                        response.code()
                    )
                }
            }
        } catch (e: ProtocolException) {
            Log.e("APIResponseHandler", "ProtocolException: ${e.message}")
            APIResult.Error(
                NETWORK_ERROR_SOURCE,
                e.message ?: "An unexpected error occurred"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            APIResult.Error(
                UNKNOWN_ERROR_SOURCE,
                e.message ?: "An unexpected error occurred"
            )
        }
    }

    companion object {
        private const val BACKEND_ERROR_SOURCE = "Backend"
        private const val JSON_ERROR_SOURCE = "JSON"
        private const val NETWORK_ERROR_SOURCE = "Network"
        private const val UNKNOWN_ERROR_SOURCE = "Unknown"
    }
}