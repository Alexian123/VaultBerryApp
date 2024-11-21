package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import com.alexianhentiu.vaultberryapp.data.model.VaultEntryDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<Unit>

    @POST("login")
    suspend fun login(@Body loginCredentialsDTO: LoginCredentialsDTO): Response<Unit>

    @POST("logout")
    suspend fun logout(): Response<Unit>

    @GET("entries")
    suspend fun getEntries(): Response<List<VaultEntryDTO>?>

    @POST("entries/add")
    suspend fun addEntry(@Body vaultEntry: VaultEntryDTO): Response<Unit>

    @POST("entries/modify")
    suspend fun modifyEntry(@Body vaultEntry: VaultEntryDTO): Response<Unit>

    @POST("entries/remove")
    suspend fun removeEntry(@Body vaultEntry: VaultEntryDTO): Response<Unit>
}