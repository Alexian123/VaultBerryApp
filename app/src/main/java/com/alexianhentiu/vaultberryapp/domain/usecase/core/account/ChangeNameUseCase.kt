package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class ChangeNameUseCase(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(
        newFirstName: String?,
        newLastName: String?
    ): ActionResult<MessageResponse> {
        when (val account = accountRepository.getAccount()) {
            is APIResult.Success -> {
                val newAccount = account.data.copy(
                    firstName = newFirstName ?: account.data.firstName,
                    lastName = newLastName ?: account.data.lastName
                )
                return when (val updateResult = accountRepository.updateAccount(newAccount)) {
                    is APIResult.Success -> {
                        ActionResult.Success(updateResult.data)
                    }

                    is APIResult.Error -> {
                        ActionResult.Error(updateResult.message)
                    }
                }
            }

            is APIResult.Error -> {
                return ActionResult.Error(account.message)
            }
        }
    }
}