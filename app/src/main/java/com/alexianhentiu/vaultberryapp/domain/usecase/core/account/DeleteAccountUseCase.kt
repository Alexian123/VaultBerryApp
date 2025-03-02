package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class DeleteAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<MessageResponse> {
        return when (val response = accountRepository.deleteAccount()) {
            is APIResult.Success -> {
                ActionResult.Success(response.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(response.message)
            }
        }
    }
}