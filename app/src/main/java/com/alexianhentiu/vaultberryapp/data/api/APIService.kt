package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.AccountDTO
import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.model.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.PasswordChangeDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("recovery")
    suspend fun getRecoveryKey(@Query("email") email: String): Response<MessageResponseDTO>

    @POST("recovery")
    suspend fun recoveryLogin(@Body credentialsDTO: LoginCredentialsDTO): Response<KeyChainDTO>

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<MessageResponseDTO>

    @POST("login")
    suspend fun login(@Body credentialsDTO: LoginCredentialsDTO): Response<KeyChainDTO>

    @POST("logout")
    suspend fun logout(): Response<MessageResponseDTO>

    @GET("entries")
    suspend fun getEntries(): Response<List<EncryptedVaultEntryDTO>?>

    @POST("entries")
    suspend fun addEntry(@Body vaultEntry: EncryptedVaultEntryDTO): Response<MessageResponseDTO>

    @PUT("entries")
    suspend fun updateEntry(@Body vaultEntry: EncryptedVaultEntryDTO): Response<MessageResponseDTO>

    @DELETE("entries/{timestamp}")
    suspend fun deleteEntry(@Path("timestamp") timestamp: Long): Response<MessageResponseDTO>

    @GET("account")
    suspend fun getAccount(): Response<AccountDTO>

    @PUT("account")
    suspend fun updateAccount(@Body account: AccountDTO): Response<MessageResponseDTO>

    @DELETE("account")
    suspend fun deleteAccount(): Response<MessageResponseDTO>

    @PUT("account/password")
    suspend fun changePassword(@Body passwordDTO: PasswordChangeDTO): Response<MessageResponseDTO>

    @PUT("account/keychain")
    suspend fun updateKeyChain(@Body keychain: KeyChainDTO): Response<MessageResponseDTO>
}