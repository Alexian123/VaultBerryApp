package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Disable2FAUseCase

class Disable2FAUseCaseImpl(
    private val accountRepository: AccountRepository
) : Disable2FAUseCase {

    override suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return when (val result = accountRepository.disable2FA()) {
            is ApiResult.Success -> {
                UseCaseResult.Success(result.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.API,
                    result.source,
                    result.message
                )
            }
        }
    }
}