package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.User

interface UserRepository {

    suspend fun register(user: User)

    suspend fun login(user: User)

    suspend fun logout()
}