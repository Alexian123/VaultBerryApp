package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain

interface AccountRepository {

    suspend fun updateAccount(account: Account): APIResult<String>

    suspend fun deleteAccount(): APIResult<String>

    suspend fun updateKeyChain(keychain: KeyChain): APIResult<String>
}