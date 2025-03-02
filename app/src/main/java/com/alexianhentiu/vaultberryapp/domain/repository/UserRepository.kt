package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.User

interface UserRepository {

    suspend fun getRecoveryOTP(email: String): APIResult<String>

    suspend fun recoveryLogin(loginCredentials: LoginCredentials): APIResult<KeyChain>

    suspend fun register(user: User): APIResult<String>

    suspend fun login(loginCredentials: LoginCredentials): APIResult<KeyChain>

    suspend fun logout(): APIResult<String>
}