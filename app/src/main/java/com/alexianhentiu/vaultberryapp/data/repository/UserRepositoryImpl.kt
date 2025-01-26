package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.RecoveryKey
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler,
    private val modelConverter: ModelConverter
) : UserRepository {

    override suspend fun getRecoveryKey(email: String): APIResult<RecoveryKey> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getRecoveryKey(email) },
            transform = { modelConverter.recoveryKeyFromDTO(it) }
        )
    }

    override suspend fun register(user: User): APIResult<Unit> {
        val userDTO = modelConverter.userToDTO(user)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.register(userDTO) },
            transform = { }
        )
    }

    override suspend fun login(loginCredentials: LoginCredentials): APIResult<KeyChain> {
        val loginCredentialsDTO = modelConverter.loginCredentialsToDTO(loginCredentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.login(loginCredentialsDTO) },
            transform = { modelConverter.keyChainFromDTO(it) }
        )
    }

    override suspend fun logout(): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.logout() },
            transform = { }
        )
    }
}