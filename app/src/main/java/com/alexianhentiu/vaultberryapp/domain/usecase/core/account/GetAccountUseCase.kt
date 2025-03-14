package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GetAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<Account> {
        return when (val accountResult = accountRepository.getAccount()) {
            is APIResult.Success -> {
                ActionResult.Success(accountResult.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    accountResult.source,
                    accountResult.message
                )
            }
        }
    }
}