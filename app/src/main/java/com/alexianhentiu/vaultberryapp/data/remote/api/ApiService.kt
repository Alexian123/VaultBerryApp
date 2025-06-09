package com.alexianhentiu.vaultberryapp.data.remote.api

import com.alexianhentiu.vaultberryapp.data.remote.model.AccountInfoDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.UserDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.VaultEntryPreviewDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.AccountInfoChangeRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.Activate2FARequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.DeleteAccountRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.LoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.PasswordChangeRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.RecoveryLoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.VaultSearchRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.BooleanResponseDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.LoginResponseDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.TotpResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val VAULT_PREFIX = "vault"
        const val ACCOUNT_PREFIX = "account"
        const val TWO_FACTOR_PREFIX = "$ACCOUNT_PREFIX/2fa"
    }

    @POST("activation/send")
    suspend fun activationSend(@Query("email") email: String): Response<MessageResponseDTO>

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

    @POST("$VAULT_PREFIX/search")
    suspend fun searchVaultEntries(
        @Body keywords: VaultSearchRequestDTO
    ): Response<List<EncryptedVaultEntryDTO>?>

    @GET(ACCOUNT_PREFIX)
    suspend fun getAccountInfo(): Response<AccountInfoDTO>

    @PATCH(ACCOUNT_PREFIX)
    suspend fun updateAccount(
        @Body data: AccountInfoChangeRequestDTO
    ): Response<MessageResponseDTO>

    @POST("$ACCOUNT_PREFIX/delete")
    suspend fun deleteAccount(@Body data: DeleteAccountRequestDTO): Response<MessageResponseDTO>

    @PATCH("$ACCOUNT_PREFIX/password")
    suspend fun changePassword(@Body data: PasswordChangeRequestDTO): Response<MessageResponseDTO>

    @POST("$TWO_FACTOR_PREFIX/setup")
    suspend fun setup2FA(): Response<TotpResponseDTO>

    @POST("$TWO_FACTOR_PREFIX/activate")
    suspend fun activate2FA(@Body data: Activate2FARequestDTO): Response<MessageResponseDTO>

    @GET("$TWO_FACTOR_PREFIX/status")
    suspend fun get2FAStatus(): Response<BooleanResponseDTO>

    @POST("$TWO_FACTOR_PREFIX/disable")
    suspend fun disable2FA(): Response<MessageResponseDTO>
}