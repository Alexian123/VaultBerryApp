package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.entity.User
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.LoginResponse

interface AuthRepository {

    suspend fun getRecoveryOTP(email: String): APIResult<MessageResponse>

    suspend fun recoveryLogin(loginCredentials: LoginRequest): APIResult<LoginResponse>

    suspend fun register(user: User): APIResult<MessageResponse>

    suspend fun loginFirstStep(loginCredentials: LoginRequest): APIResult<LoginResponse>

    suspend fun loginSecondStep(loginCredentials: LoginRequest): APIResult<LoginResponse>

    suspend fun logout(): APIResult<MessageResponse>
}