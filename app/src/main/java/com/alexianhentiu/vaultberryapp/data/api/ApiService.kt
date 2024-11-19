package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.LoginRequest
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import com.alexianhentiu.vaultberryapp.data.model.VaultEntryDTO
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<Unit>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Unit>

    @POST("logout")
    suspend fun logout(): Response<Unit>

    @GET("entries")
    suspend fun getEntries(): Response<List<VaultEntryDTO>?>

    @POST("entries")
    suspend fun addEntry(@Body vaultEntry: VaultEntryDTO): Response<Unit>
}