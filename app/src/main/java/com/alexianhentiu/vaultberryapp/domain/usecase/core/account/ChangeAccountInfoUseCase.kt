package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ChangeAccountInfoUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(account: Account): ActionResult<MessageResponse> {
        return when (val updateResult = accountRepository.updateAccount(account)) {
            is APIResult.Success -> {
                ActionResult.Success(updateResult.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    updateResult.source,
                    updateResult.message
                )
            }
        }
    }
}