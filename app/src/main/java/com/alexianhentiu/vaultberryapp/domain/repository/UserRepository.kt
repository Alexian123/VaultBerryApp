package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.RecoveryKey
import com.alexianhentiu.vaultberryapp.domain.model.User

interface UserRepository {

    suspend fun getRecoveryKey(email: String): APIResult<RecoveryKey>

    suspend fun register(user: User): APIResult<String>

    suspend fun login(loginCredentials: LoginCredentials): APIResult<KeyChain>

    suspend fun logout(): APIResult<String>
}