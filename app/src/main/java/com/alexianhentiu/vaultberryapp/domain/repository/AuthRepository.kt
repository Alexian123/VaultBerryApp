package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.request.RecoveryLoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.LoginResponse

interface AuthRepository {

    suspend fun activationSend(email: String): ApiResult<MessageResponse>

    suspend fun recoverySend(email: String): ApiResult<MessageResponse>

    suspend fun recoveryLogin(credentials: RecoveryLoginRequest): ApiResult<KeyChain>

    suspend fun register(user: User): ApiResult<MessageResponse>

    suspend fun loginFirstStep(loginCredentials: LoginRequest): ApiResult<LoginResponse>

    suspend fun loginSecondStep(loginCredentials: LoginRequest): ApiResult<LoginResponse>

    suspend fun logout(): ApiResult<MessageResponse>
}