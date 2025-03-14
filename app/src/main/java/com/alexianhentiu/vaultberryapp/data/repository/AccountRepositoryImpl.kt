package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository

class AccountRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler,
    private val modelConverter: ModelConverter
) : AccountRepository {

    override suspend fun getAccount(): APIResult<Account> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAccount() },
            transform = { modelConverter.accountFromDTO(it) }
        )
    }

    override suspend fun updateAccount(account: Account): APIResult<MessageResponse> {
        val accountDTO = modelConverter.accountToDTO(account)
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

    override suspend fun changePassword(passwordPair: PasswordPair): APIResult<MessageResponse> {
        val passwordPairDTO = modelConverter.passwordPairToDTO(passwordPair)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.changePassword(passwordPairDTO) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun updateKeyChain(keychain: KeyChain): APIResult<MessageResponse> {
        val keychainDTO = modelConverter.keyChainToDTO(keychain)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateKeyChain(keychainDTO) },
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