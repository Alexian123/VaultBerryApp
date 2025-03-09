package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.AccountDTO
import com.alexianhentiu.vaultberryapp.data.model.BooleanResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.model.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.PasswordPairDTO
import com.alexianhentiu.vaultberryapp.data.model.TotpResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @POST("2fa/verify")
    suspend fun verify2FA(@Body credentialsDTO: LoginCredentialsDTO): Response<KeyChainDTO>

    @POST("recovery")
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

    @PATCH("entries")
    suspend fun updateEntry(@Body vaultEntry: EncryptedVaultEntryDTO): Response<MessageResponseDTO>

    @DELETE("entries/{timestamp}")
    suspend fun deleteEntry(@Path("timestamp") timestamp: Long): Response<MessageResponseDTO>

    @GET("account")
    suspend fun getAccount(): Response<AccountDTO>

    @PATCH("account")
    suspend fun updateAccount(@Body account: AccountDTO): Response<MessageResponseDTO>

    @DELETE("account")
    suspend fun deleteAccount(): Response<MessageResponseDTO>

    @PATCH("account/password")
    suspend fun changePassword(@Body passwordPairDTO: PasswordPairDTO): Response<MessageResponseDTO>

    @PUT("account/keychain")
    suspend fun updateKeyChain(@Body keychain: KeyChainDTO): Response<MessageResponseDTO>

    @POST("account/2fa/setup")
    suspend fun setup2FA(): Response<TotpResponseDTO>

    @GET("account/2fa/status")
    suspend fun get2FAStatus(): Response<BooleanResponseDTO>

    @POST("account/2fa/disable")
    suspend fun disable2FA(): Response<MessageResponseDTO>
}