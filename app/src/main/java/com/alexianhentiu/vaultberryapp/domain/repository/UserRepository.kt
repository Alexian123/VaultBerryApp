package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.User

interface UserRepository {

    suspend fun register(user: User): APIResult<Unit>

    suspend fun login(loginCredentials: LoginCredentials): APIResult<EncryptedVaultKey>

    suspend fun logout(): APIResult<Unit>
}