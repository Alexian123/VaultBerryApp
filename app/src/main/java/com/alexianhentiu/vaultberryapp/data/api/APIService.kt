package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.AccountDTO
import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.model.RecoveryKeyDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("recovery")
    suspend fun getRecoveryKey(@Query("email") email: String): Response<RecoveryKeyDTO>

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<Unit>

    @POST("login")
    suspend fun login(@Body loginCredentialsDTO: LoginCredentialsDTO): Response<KeyChainDTO>

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

    @POST("account")
    suspend fun updateAccount(@Body account: AccountDTO): Response<Unit>

    @DELETE("account")
    suspend fun deleteAccount(): Response<Unit>

    @POST("account/keychain")
    suspend fun updateKeyChain(@Body keychain: KeyChainDTO): Response<Unit>
}