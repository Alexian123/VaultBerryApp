package com.alexianhentiu.vaultberryapp.application.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.PingApiUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.NetworkUtils
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class PingApiUseCaseImpl(
    private val networkUtils: NetworkUtils,
    private val stringResourceProvider: StringResourceProvider
) : PingApiUseCase {
    override suspend fun invoke(
        host: String,
        port: Int,
        timeoutMs: Int
    ): UseCaseResult<Unit> {
        try {
            val isReachable = networkUtils.isHostReachable(host, port, timeoutMs)
            return if (isReachable) {
                UseCaseResult.Success(Unit)
            } else {
                UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.HOST_UNREACHABLE,
                        stringResourceProvider.getString(R.string.network_utils_error_source),
                        "Host $host:$port not reachable"
                    )
                )
            }
        }  catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}