package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.domain.model.entity.User
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.LoginResponse
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler,
    private val modelConverter: ModelConverter
) : AuthRepository {

    override suspend fun getRecoveryOTP(email: String): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getRecoveryKey(email) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun recoveryLogin(loginCredentials: LoginRequest): APIResult<LoginResponse> {
        val loginCredentialsDTO = modelConverter.loginRequestToDTO(loginCredentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.recoveryLogin(loginCredentialsDTO) },
            transform = { modelConverter.loginResponseFromDTO(it) }
        )
    }

    override suspend fun register(user: User): APIResult<MessageResponse> {
        val userDTO = modelConverter.userToDTO(user)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.register(userDTO) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun loginFirstStep(loginCredentials: LoginRequest): APIResult<LoginResponse> {
        val loginCredentialsDTO = modelConverter.loginRequestToDTO(loginCredentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.loginStep1(loginCredentialsDTO) },
            transform = { modelConverter.loginResponseFromDTO(it) }
        )
    }

    override suspend fun loginSecondStep(loginCredentials: LoginRequest): APIResult<LoginResponse> {
        val loginCredentialsDTO = modelConverter.loginRequestToDTO(loginCredentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.loginStep2(loginCredentialsDTO) },
            transform = { modelConverter.loginResponseFromDTO(it) }
        )
    }

    override suspend fun logout(): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.logout() },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }
}