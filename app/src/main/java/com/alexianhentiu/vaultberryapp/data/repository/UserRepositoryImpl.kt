package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.LoginResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.LoginResponse
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler
) : UserRepository {

    override suspend fun register(user: User): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.register(user.toDBModel()) },
            transform = { }
        )
    }

    override suspend fun login(loginCredentials: LoginCredentials): APIResult<LoginResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.login(loginCredentials.toDBModel()) },
            transform = { it.toDomainModel() }
        )
    }

    override suspend fun logout(): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.logout() },
            transform = { }
        )
    }

    private fun User.toDBModel(): UserDTO {
        return UserDTO(email, password, salt, vaultKey, recoveryKey, firstName, lastName)
    }

    private fun LoginCredentials.toDBModel(): LoginCredentialsDTO {
        return LoginCredentialsDTO(email, password)
    }

    private fun LoginResponseDTO.toDomainModel(): LoginResponse {
        return LoginResponse(salt, vaultKey, recoveryKey)
    }


}