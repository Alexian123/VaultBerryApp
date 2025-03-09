package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

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
                ActionResult.Error(result.message)
            }
        }
    }
}