package com.alexianhentiu.vaultberryapp.data.repository.remote

import com.alexianhentiu.vaultberryapp.data.remote.ApiResponseHandler
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.data.remote.ModelMapper
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.data.remote.model.request.AccountInfoChangeRequestDTO
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.request.Activate2FARequest
import com.alexianhentiu.vaultberryapp.domain.model.request.DeleteAccountRequest
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class RemoteAccountRepository(
    private val apiService: ApiService,
    private val apiResponseHandler: ApiResponseHandler,
    private val modelMapper: ModelMapper
) : AccountRepository {

    override suspend fun getAccountInfo(): ApiResult<AccountInfo> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAccountInfo() },
            transform = { modelMapper.accountInfoFromDTO(it) }
        )
    }

    override suspend fun updateAccount(
        accountInfo: AccountInfo,
        noActivation: Boolean
    ): ApiResult<MessageResponse> {
        val accountDTO = modelMapper.accountInfoToDTO(accountInfo)
        val request = AccountInfoChangeRequestDTO(accountDTO, noActivation)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateAccount(request) },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }

    override suspend fun deleteAccount(data: DeleteAccountRequest): ApiResult<MessageResponse> {
        val dto = modelMapper.deleteAccountRequestToDTO(data)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteAccount(dto) },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }

    override suspend fun changePassword(data: PasswordChangeRequest): ApiResult<MessageResponse> {
        val dto = modelMapper.passwordChangeRequestToDTO(data)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.changePassword(dto) },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }

    override suspend fun setup2FA(): ApiResult<TotpResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.setup2FA() },
            transform = { modelMapper.totpResponseFromDTO(it) }
        )
    }

    override suspend fun activate2FA(data: Activate2FARequest): ApiResult<MessageResponse> {
        val dto = modelMapper.activate2FARequestToDTO(data)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.activate2FA(dto) },
            transform = { modelMapper.messageResponseFromDTO(it) }
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
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }
}