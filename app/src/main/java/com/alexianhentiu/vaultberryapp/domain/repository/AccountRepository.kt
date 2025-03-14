package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.TotpResponse

interface AccountRepository {

    suspend fun getAccount(): APIResult<Account>

    suspend fun updateAccount(account: Account): APIResult<MessageResponse>

    suspend fun deleteAccount(): APIResult<MessageResponse>

    suspend fun changePassword(passwordPair: PasswordPair): APIResult<MessageResponse>

    suspend fun updateKeyChain(keychain: KeyChain): APIResult<MessageResponse>

    suspend fun setup2FA(): APIResult<TotpResponse>

    suspend fun get2FAStatus(): APIResult<Boolean>

    suspend fun disable2FA(): APIResult<MessageResponse>
}