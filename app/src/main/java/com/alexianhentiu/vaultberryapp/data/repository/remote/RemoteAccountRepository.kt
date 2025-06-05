package com.alexianhentiu.vaultberryapp.data.repository.remote

import com.alexianhentiu.vaultberryapp.data.remote.ApiResponseHandler
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.data.remote.ModelConverter
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.data.remote.model.request.AccountInfoChangeRequestDTO
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class RemoteAccountRepository(
    private val apiService: ApiService,
    private val apiResponseHandler: ApiResponseHandler,
    private val modelConverter: ModelConverter
) : AccountRepository {

    override suspend fun getAccountInfo(): ApiResult<AccountInfo> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAccountInfo() },
            transform = { modelConverter.accountInfoFromDTO(it) }
        )
    }

    override suspend fun updateAccount(
        accountInfo: AccountInfo,
        noActivation: Boolean
    ): ApiResult<MessageResponse> {
        val accountDTO = modelConverter.accountInfoToDTO(accountInfo)
        val request = AccountInfoChangeRequestDTO(accountDTO, noActivation)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateAccount(request) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun deleteAccount(): ApiResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteAccount() },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun changePassword(data: PasswordChangeRequest): ApiResult<MessageResponse> {
        val dto = modelConverter.passwordChangeRequestToDTO(data)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.changePassword(dto) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun setup2FA(): ApiResult<TotpResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.setup2FA() },
            transform = { modelConverter.totpResponseFromDTO(it) }
        )
    }

    override suspend fun get2FAStatus(): ApiResult<Boolean> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.get2FAStatus() },
            transform = { it.enabled }
        )
    }

    override suspend fun disable2FA(): ApiResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.disable2FA() },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }
}