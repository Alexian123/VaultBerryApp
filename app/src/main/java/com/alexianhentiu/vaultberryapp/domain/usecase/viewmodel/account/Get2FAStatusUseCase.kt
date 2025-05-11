package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class Get2FAStatusUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): UseCaseResult<Boolean> {
        return when (val result = accountRepository.get2FAStatus()) {
            is APIResult.Success -> {
                UseCaseResult.Success(result.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}