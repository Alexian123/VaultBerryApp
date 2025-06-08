package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Get2FAStatusUseCase

class Get2FAStatusUseCaseImpl(
    private val accountRepository: AccountRepository
) : Get2FAStatusUseCase {

    override suspend operator fun invoke(): UseCaseResult<Boolean> {
        return when (val result = accountRepository.get2FAStatus()) {
            is ApiResult.Success -> {
                UseCaseResult.Success(result.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        result.source,
                        result.message
                    )
                )
            }
        }
    }
}