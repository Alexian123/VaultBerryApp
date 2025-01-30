package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class ChangeEmailUseCase(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(newEmail: String): APIResult<String> {
        when (val account = accountRepository.getAccount()) {
            is APIResult.Success -> {
                val newAccount = account.data.copy(email = newEmail)
                return accountRepository.updateAccount(newAccount)
            }

            is APIResult.Error -> {
                return APIResult.Error(account.message)
            }
        }
    }
}