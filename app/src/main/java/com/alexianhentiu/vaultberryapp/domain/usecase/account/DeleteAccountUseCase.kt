package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class DeleteAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): APIResult<String> {
        return accountRepository.deleteAccount()
    }
}