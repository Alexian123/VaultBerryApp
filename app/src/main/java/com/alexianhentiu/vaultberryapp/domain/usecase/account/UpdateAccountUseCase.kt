package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class UpdateAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(account: Account): APIResult<String> {
        return accountRepository.updateAccount(account)
    }
}