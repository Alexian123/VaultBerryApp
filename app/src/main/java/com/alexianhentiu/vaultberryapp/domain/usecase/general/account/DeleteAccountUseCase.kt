package com.alexianhentiu.vaultberryapp.domain.usecase.general.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class DeleteAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<MessageResponse> {
        return when (val response = accountRepository.deleteAccount()) {
            is APIResult.Success -> {
                ActionResult.Success(response.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    response.source,
                    response.message
                )
            }
        }
    }
}