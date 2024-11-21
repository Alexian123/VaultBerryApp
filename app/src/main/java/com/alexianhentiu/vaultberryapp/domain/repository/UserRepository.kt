package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.User

interface UserRepository {

    suspend fun register(user: User): Boolean

    suspend fun login(loginCredentials: LoginCredentials): Boolean

    suspend fun logout(): Boolean
}