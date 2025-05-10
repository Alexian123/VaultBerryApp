package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class RecoverySendUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String): UseCaseResult<MessageResponse> {
        return when (val response = authRepository.recoverySend(email)) {
            is APIResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    response.source,
                    response.message
                )
            }
        }
    }
}