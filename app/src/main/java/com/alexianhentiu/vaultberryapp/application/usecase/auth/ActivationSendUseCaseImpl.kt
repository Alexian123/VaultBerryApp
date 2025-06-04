package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.ActivationSendUseCase

class ActivationSendUseCaseImpl(
    private val authRepository: AuthRepository
) : ActivationSendUseCase {
    override suspend operator fun invoke(email: String): UseCaseResult<MessageResponse> {
        return when(val result = authRepository.activationSend(email)) {
            is ApiResult.Success -> UseCaseResult.Success(result.data)
            is ApiResult.Error -> {
                UseCaseResult.Error(
                    type = ErrorType.API,
                    source = result.source,
                    message = result.message
                )
            }
        }
    }
}