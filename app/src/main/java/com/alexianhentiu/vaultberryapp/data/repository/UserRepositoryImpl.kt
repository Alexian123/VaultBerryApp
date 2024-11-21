package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class UserRepositoryImpl(private val apiService: APIService) : UserRepository {
    override suspend fun register(user: User): Boolean {
        return apiService.register(user.toDBModel()).isSuccessful
    }

    override suspend fun login(loginCredentials: LoginCredentials): Boolean {
        return apiService.login(loginCredentials.toDBModel()).isSuccessful
    }

    override suspend fun logout(): Boolean {
        return apiService.logout().isSuccessful
    }

    private fun User.toDBModel(): UserDTO {
        return UserDTO(email, password, firstName, lastName)
    }

    private fun LoginCredentials.toDBModel(): LoginCredentialsDTO {
        return LoginCredentialsDTO(email, password)
    }
}