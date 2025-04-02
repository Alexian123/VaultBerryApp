package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse

interface AccountRepository {

    suspend fun getAccountInfo(): APIResult<AccountInfo>

    suspend fun updateAccount(accountInfo: AccountInfo): APIResult<MessageResponse>

    suspend fun deleteAccount(): APIResult<MessageResponse>

    suspend fun changePassword(data: PasswordChangeRequest): APIResult<MessageResponse>

    suspend fun setup2FA(): APIResult<TotpResponse>

    suspend fun get2FAStatus(): APIResult<Boolean>

    suspend fun disable2FA(): APIResult<MessageResponse>
}