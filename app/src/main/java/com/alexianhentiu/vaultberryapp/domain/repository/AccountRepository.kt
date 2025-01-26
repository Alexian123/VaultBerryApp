package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain

interface AccountRepository {

    suspend fun updateAccount(account: Account): APIResult<Unit>

    suspend fun deleteAccount(): APIResult<Unit>

    suspend fun updateKeyChain(keychain: KeyChain): APIResult<Unit>
}