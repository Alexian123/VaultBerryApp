package com.alexianhentiu.vaultberryapp.data.remote

import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import okio.ProtocolException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiResponseHandler @Inject constructor(
    private val stringResourceProvider: StringResourceProvider
) {

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
                        stringResourceProvider.getString(
                            com.alexianhentiu.vaultberryapp.R.string.empty_response_body
                        ),
                        response.code()
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                try {
                    val jsonObject = errorBody?.let { JSONObject(it) }
                    val errorMessage = jsonObject?.getString("error")
                        ?: stringResourceProvider.getString(
                            com.alexianhentiu.vaultberryapp.R.string.unknown_error
                        )
                    ApiResult.Error(BACKEND_ERROR_SOURCE, errorMessage, response.code())
                } catch (e: JSONException) {
                    ApiResult.Error(
                        JSON_ERROR_SOURCE,
                        errorBody
                            ?: e.message
                            ?: stringResourceProvider.getString(
                                com.alexianhentiu.vaultberryapp.R.string.unexpected_error
                            ),
                        response.code()
                    )
                }
            }
        } catch (e: ProtocolException) {
            ApiResult.Error(
                NETWORK_ERROR_SOURCE,
                e.message ?: stringResourceProvider.getString(
                    com.alexianhentiu.vaultberryapp.R.string.unexpected_error
                )
            )
        } catch (e: Exception) {
            ApiResult.Error(
                UNKNOWN_ERROR_SOURCE,
                e.message ?: stringResourceProvider.getString(
                    com.alexianhentiu.vaultberryapp.R.string.unexpected_error
                )
            )
        }
    }
}