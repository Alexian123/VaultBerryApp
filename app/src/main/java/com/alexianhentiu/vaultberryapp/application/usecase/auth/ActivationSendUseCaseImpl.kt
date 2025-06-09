package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.ActivationSendUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class ActivationSendUseCaseImpl(
    private val authRepository: AuthRepository,
    private val stringResourceProvider: StringResourceProvider
) : ActivationSendUseCase {
    override suspend operator fun invoke(email: String): UseCaseResult<MessageResponse> {
        return try {
            when (val result = authRepository.activationSend(email)) {
                is ApiResult.Success -> UseCaseResult.Success(result.data)
                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            type = ErrorType.API,
                            source = result.source,
                            message = result.message
                        )
                    )
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