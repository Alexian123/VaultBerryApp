package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class Disable2FAUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<MessageResponse> {
        return when (val result = accountRepository.disable2FA()) {
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