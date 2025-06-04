package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase

class LogoutUseCaseImpl(private val authRepository: AuthRepository) : LogoutUseCase {

    override suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return when (val response = authRepository.logout()) {
            is ApiResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.API,
                    response.source,
                    response.message
                )
            }
        }
    }
}