package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class Setup2FAUseCase(
    private val accountRepository: AccountRepository,
    private val extract2FASecret: Extract2FASecret
) {

    suspend operator fun invoke(): UseCaseResult<String> {
        return when (val result = accountRepository.setup2FA()) {
            is APIResult.Success -> {
                return extract2FASecret(result.data)
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