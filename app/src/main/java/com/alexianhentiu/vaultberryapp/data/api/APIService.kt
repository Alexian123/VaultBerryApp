package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.entity.AccountInfoDTO
import com.alexianhentiu.vaultberryapp.data.model.response.BooleanResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.model.response.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.request.PasswordChangeRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.response.TotpResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.UserDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.VaultEntryPreviewDTO
import com.alexianhentiu.vaultberryapp.data.model.request.LoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.request.RecoveryLoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.response.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    companion object {
        const val VAULT_PREFIX = "vault"
        const val ACCOUNT_PREFIX = "account"
    }

    @POST("recovery/send")
    suspend fun recoverySend(@Query("email") email: String): Response<MessageResponseDTO>

    @POST("recovery/login")
    suspend fun recoveryLogin(@Body credentialsDTO: RecoveryLoginRequestDTO): Response<KeyChainDTO>

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<MessageResponseDTO>

    @POST("login/step1")
    suspend fun loginStep1(@Body credentialsDTO: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("login/step2")
    suspend fun loginStep2(@Body credentialsDTO: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("logout")
    suspend fun logout(): Response<MessageResponseDTO>

    @GET("$VAULT_PREFIX/previews")
    suspend fun getAllVaultEntryPreviews(): Response<List<VaultEntryPreviewDTO>?>

    @GET("$VAULT_PREFIX/details")
    suspend fun getAllVaultEntryDetails(): Response<List<EncryptedVaultEntryDTO>?>

    @GET("$VAULT_PREFIX/details/{id}")
    suspend fun getVaultEntryDetails(@Path("id") id: Long): Response<EncryptedVaultEntryDTO>

    @POST("$VAULT_PREFIX/add")
    suspend fun addEntry(@Body vaultEntry: EncryptedVaultEntryDTO): Response<VaultEntryPreviewDTO>

    @PATCH("$VAULT_PREFIX/update/{id}")
    suspend fun updateEntry(
        @Path("id") id: Long,
        @Body vaultEntry: EncryptedVaultEntryDTO
    ): Response<MessageResponseDTO>

    @DELETE("$VAULT_PREFIX/delete/{id}")
    suspend fun deleteEntry(@Path("id") id: Long): Response<MessageResponseDTO>

    @GET(ACCOUNT_PREFIX)
    suspend fun getAccountInfo(): Response<AccountInfoDTO>

    @PATCH(ACCOUNT_PREFIX)
    suspend fun updateAccount(@Body account: AccountInfoDTO): Response<MessageResponseDTO>

    @DELETE(ACCOUNT_PREFIX)
    suspend fun deleteAccount(): Response<MessageResponseDTO>

    @PATCH("$ACCOUNT_PREFIX/password")
    suspend fun changePassword(@Body data: PasswordChangeRequestDTO): Response<MessageResponseDTO>

    @POST("$ACCOUNT_PREFIX/2fa/setup")
    suspend fun setup2FA(): Response<TotpResponseDTO>

    @GET("$ACCOUNT_PREFIX/2fa/status")
    suspend fun get2FAStatus(): Response<BooleanResponseDTO>

    @POST("$ACCOUNT_PREFIX/2fa/disable")
    suspend fun disable2FA(): Response<MessageResponseDTO>
}