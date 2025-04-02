package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class AccountRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler,
    private val modelConverter: ModelConverter
) : AccountRepository {

    override suspend fun getAccountInfo(): APIResult<AccountInfo> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAccountInfo() },
            transform = { modelConverter.accountInfoFromDTO(it) }
        )
    }

    override suspend fun updateAccount(accountInfo: AccountInfo): APIResult<MessageResponse> {
        val accountDTO = modelConverter.accountInfoToDTO(accountInfo)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateAccount(accountDTO) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun deleteAccount(): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteAccount() },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun changePassword(data: PasswordChangeRequest): APIResult<MessageResponse> {
        val dto = modelConverter.passwordChangeRequestToDTO(data)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.changePassword(dto) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun setup2FA(): APIResult<TotpResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.setup2FA() },
            transform = { modelConverter.totpResponseFromDTO(it) }
        )
    }

    override suspend fun get2FAStatus(): APIResult<Boolean> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.get2FAStatus() },
            transform = { it.enabled }
        )
    }

    override suspend fun disable2FA(): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.disable2FA() },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }
}