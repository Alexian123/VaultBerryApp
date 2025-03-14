package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.User

interface UserRepository {

    suspend fun verify2FA(loginCredentials: LoginCredentials): APIResult<KeyChain>

    suspend fun getRecoveryOTP(email: String): APIResult<MessageResponse>

    suspend fun recoveryLogin(loginCredentials: LoginCredentials): APIResult<KeyChain>

    suspend fun register(user: User): APIResult<MessageResponse>

    suspend fun login(loginCredentials: LoginCredentials): APIResult<KeyChain>

    suspend fun logout(): APIResult<MessageResponse>
}