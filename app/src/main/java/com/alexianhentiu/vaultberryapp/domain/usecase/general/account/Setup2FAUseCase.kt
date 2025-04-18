package com.alexianhentiu.vaultberryapp.domain.usecase.general.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class Setup2FAUseCase(
    private val accountRepository: AccountRepository,
    private val extract2FASecret: Extract2FASecret
) {

    suspend operator fun invoke(): ActionResult<String> {
        return when (val result = accountRepository.setup2FA()) {
            is APIResult.Success -> {
                return extract2FASecret(result.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}