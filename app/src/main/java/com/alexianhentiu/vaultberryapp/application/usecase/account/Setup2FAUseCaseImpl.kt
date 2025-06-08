package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.application.usecase.internal.Extract2FASecretUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Setup2FAUseCase

class Setup2FAUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val extract2FASecretUseCase: Extract2FASecretUseCase
) : Setup2FAUseCase {

    override suspend operator fun invoke(): UseCaseResult<String> {
        return when (val result = accountRepository.setup2FA()) {
            is ApiResult.Success -> {
                return extract2FASecretUseCase(result.data)
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