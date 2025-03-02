package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse

interface AccountRepository {

    suspend fun getAccount(): APIResult<Account>

    suspend fun updateAccount(account: Account): APIResult<MessageResponse>

    suspend fun deleteAccount(): APIResult<MessageResponse>

    suspend fun changePassword(password: String): APIResult<MessageResponse>

    suspend fun updateKeyChain(keychain: KeyChain): APIResult<MessageResponse>
}