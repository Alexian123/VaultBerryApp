package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class Get2FAStatusUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<Boolean> {
        return when (val result = accountRepository.get2FAStatus()) {
            is APIResult.Success -> {
                ActionResult.Success(result.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(result.message)
            }
        }
    }
}