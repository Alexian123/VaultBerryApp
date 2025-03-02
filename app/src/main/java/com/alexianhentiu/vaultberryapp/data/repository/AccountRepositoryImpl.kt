package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
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

    override suspend fun changePassword(password: String): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.changePassword(modelConverter.passwordToDTO(password)) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun updateKeyChain(keychain: KeyChain): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateKeyChain(modelConverter.keyChainToDTO(keychain)) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }
}