package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.enums.HttpResponseCode
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository,
    private val stringResourceProvider: StringResourceProvider
) : LogoutUseCase {

    override suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return try {
            when (val response = authRepository.logout()) {
                is ApiResult.Success -> {
                    UseCaseResult.Success(response.data)
                }

                is ApiResult.Error -> {
                    if (response.code == HttpResponseCode.UNAUTHORIZED.code) {
                        return UseCaseResult.Error(
                            ErrorInfo(
                                ErrorType.LOGGED_OUT,
                                response.source,
                                response.message
                            )
                        )
                    } else {
                        UseCaseResult.Error(
                            ErrorInfo(
                                ErrorType.API,
                                response.source,
                                response.message
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}