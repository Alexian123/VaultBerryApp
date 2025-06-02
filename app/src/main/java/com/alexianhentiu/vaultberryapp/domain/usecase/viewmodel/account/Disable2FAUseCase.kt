package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class Disable2FAUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return when (val result = accountRepository.disable2FA()) {
            is APIResult.Success -> {
                UseCaseResult.Success(result.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.API,
                    result.source,
                    result.message
                )
            }
        }
    }
}