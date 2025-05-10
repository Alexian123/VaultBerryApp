package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class Get2FAStatusUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<Boolean> {
        return when (val result = accountRepository.get2FAStatus()) {
            is APIResult.Success -> {
                ActionResult.Success(result.data)
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