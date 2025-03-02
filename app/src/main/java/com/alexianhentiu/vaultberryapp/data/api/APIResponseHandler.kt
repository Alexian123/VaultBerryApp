package com.alexianhentiu.vaultberryapp.data.api

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
                    APIResult.Error("Empty response body", response.code())
                }
            } else {
                val errorBody = response.errorBody()?.string()
                try {
                    val jsonObject = errorBody?.let { JSONObject(it) } // Parse as JSON
                    val errorMessage = jsonObject?.getString("error") ?: "Unknown error"
                    APIResult.Error(errorMessage)
                } catch (e: JSONException) {
                    APIResult.Error(errorBody ?: "Unknown error", response.code())
                }
            }
        } catch (e: ProtocolException) {
            Log.e("APIResponseHandler", "ProtocolException: ${e.message}")
            APIResult.Error(e.message ?: "An unexpected error occurred")
        } catch (e: Exception) {
            e.printStackTrace()
            APIResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}