package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
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

    override suspend fun updateAccount(account: Account): APIResult<String> {
        val accountDTO = modelConverter.accountToDTO(account)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateAccount(accountDTO) },
            transform = { it.message }
        )
    }

    override suspend fun deleteAccount(): APIResult<String> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteAccount() },
            transform = { it.message }
        )
    }

    override suspend fun updateKeyChain(keychain: KeyChain): APIResult<String> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateKeyChain(modelConverter.keyChainToDTO(keychain)) },
            transform = { it.message }
        )
    }
}