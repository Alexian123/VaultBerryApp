package com.alexianhentiu.vaultberryapp.data.remote

import okio.ProtocolException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiResponseHandler @Inject constructor() {

    companion object {
        private const val BACKEND_ERROR_SOURCE = "Backend"
        private const val JSON_ERROR_SOURCE = "JSON"
        private const val NETWORK_ERROR_SOURCE = "Network"
        private const val UNKNOWN_ERROR_SOURCE = "Unknown"
    }

    // Generic function to handle API calls and map responses
    suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> Response<T>,
        transform: (T) -> R
    ): ApiResult<R> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(transform(body))
                } else {
                    ApiResult.Error(
                        BACKEND_ERROR_SOURCE,
                        "Empty response body",
                        response.code()
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                try {
                    val jsonObject = errorBody?.let { JSONObject(it) }
                    val errorMessage = jsonObject?.getString("error") ?: "Unknown error"
                    ApiResult.Error(BACKEND_ERROR_SOURCE, errorMessage, response.code())
                } catch (e: JSONException) {
                    ApiResult.Error(
                        JSON_ERROR_SOURCE,
                        errorBody ?: e.message ?: "An unexpected error occurred",
                        response.code()
                    )
                }
            }
        } catch (e: ProtocolException) {
            ApiResult.Error(
                NETWORK_ERROR_SOURCE,
                e.message ?: "An unexpected error occurred"
            )
        } catch (e: Exception) {
            ApiResult.Error(
                UNKNOWN_ERROR_SOURCE,
                e.message ?: "An unexpected error occurred"
            )
        }
    }
}