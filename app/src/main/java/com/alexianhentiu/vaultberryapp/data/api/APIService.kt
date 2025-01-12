package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultKeyDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<Unit>

    @POST("login")
    suspend fun login(@Body loginCredentialsDTO: LoginCredentialsDTO): Response<EncryptedVaultKeyDTO>

    @POST("logout")
    suspend fun logout(): Response<Unit>

    @GET("entries")
    suspend fun getEntries(): Response<List<EncryptedVaultEntryDTO>?>

    @POST("entries/add")
    suspend fun addEntry(@Body vaultEntry: EncryptedVaultEntryDTO): Response<Unit>

    @POST("entries/update")
    suspend fun updateEntry(@Body vaultEntry: EncryptedVaultEntryDTO): Response<Unit>

    @DELETE("entries/delete/{timestamp}")
    suspend fun deleteEntry(@Path("timestamp") timestamp: Long): Response<Unit>
}