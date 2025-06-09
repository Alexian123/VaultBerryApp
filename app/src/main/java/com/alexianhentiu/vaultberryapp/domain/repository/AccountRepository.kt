package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.request.Activate2FARequest
import com.alexianhentiu.vaultberryapp.domain.model.request.DeleteAccountRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse

interface AccountRepository {

    suspend fun getAccountInfo(): ApiResult<AccountInfo>

    suspend fun updateAccount(
        accountInfo: AccountInfo,
        noActivation: Boolean
    ): ApiResult<MessageResponse>

    suspend fun deleteAccount(data: DeleteAccountRequest): ApiResult<MessageResponse>

    suspend fun changePassword(data: PasswordChangeRequest): ApiResult<MessageResponse>

    suspend fun setup2FA(): ApiResult<TotpResponse>

    suspend fun activate2FA(data: Activate2FARequest): ApiResult<MessageResponse>

    suspend fun get2FAStatus(): ApiResult<Boolean>

    suspend fun disable2FA(): ApiResult<MessageResponse>
}