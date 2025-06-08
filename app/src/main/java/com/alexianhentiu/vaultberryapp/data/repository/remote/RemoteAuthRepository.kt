package com.alexianhentiu.vaultberryapp.data.repository.remote

import com.alexianhentiu.vaultberryapp.data.remote.ApiResponseHandler
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.data.remote.ModelMapper
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.request.RecoveryLoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.LoginResponse
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository

class RemoteAuthRepository(
    private val apiService: ApiService,
    private val apiResponseHandler: ApiResponseHandler,
    private val modelMapper: ModelMapper
) : AuthRepository {

    override suspend fun activationSend(email: String): ApiResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.activationSend(email) },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }

    override suspend fun recoverySend(email: String): ApiResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.recoverySend(email) },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }

    override suspend fun recoveryLogin(credentials: RecoveryLoginRequest): ApiResult<KeyChain> {
        val credentialsDTO = modelMapper.recoveryLoginRequestToDTO(credentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.recoveryLogin(credentialsDTO) },
            transform = { modelMapper.keyChainFromDTO(it) }
        )
    }

    override suspend fun register(user: User): ApiResult<MessageResponse> {
        val userDTO = modelMapper.userToDTO(user)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.register(userDTO) },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }

    override suspend fun loginFirstStep(loginCredentials: LoginRequest): ApiResult<LoginResponse> {
        val loginCredentialsDTO = modelMapper.loginRequestToDTO(loginCredentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.loginStep1(loginCredentialsDTO) },
            transform = { modelMapper.loginResponseFromDTO(it) }
        )
    }

    override suspend fun loginSecondStep(loginCredentials: LoginRequest): ApiResult<LoginResponse> {
        val loginCredentialsDTO = modelMapper.loginRequestToDTO(loginCredentials)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.loginStep2(loginCredentialsDTO) },
            transform = { modelMapper.loginResponseFromDTO(it) }
        )
    }

    override suspend fun logout(): ApiResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.logout() },
            transform = { modelMapper.messageResponseFromDTO(it) }
        )
    }
}