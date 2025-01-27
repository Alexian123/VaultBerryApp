package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class GetAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): APIResult<Account> {
        return accountRepository.getAccount()
    }
}