package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class UpdateKeyChainUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(keyChain: KeyChain): APIResult<String> {
        return accountRepository.updateKeyChain(keyChain)
    }
}