package com.alexianhentiu.vaultberryapp.data.api

import com.alexianhentiu.vaultberryapp.data.model.entity.AccountInfoDTO
import com.alexianhentiu.vaultberryapp.data.model.response.BooleanResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.response.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.request.PasswordChangeRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.response.TotpResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.UserDTO
import com.alexianhentiu.vaultberryapp.data.model.request.LoginRequestDTO
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

    @POST("recovery")
    suspend fun getRecoveryKey(@Query("email") email: String): Response<MessageResponseDTO>

    @POST("recovery")
    suspend fun recoveryLogin(@Body credentialsDTO: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("register")
    suspend fun register(@Body user: UserDTO): Response<MessageResponseDTO>

    @POST("login/step1")
    suspend fun loginStep1(@Body credentialsDTO: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("login/step2")
    suspend fun loginStep2(@Body credentialsDTO: LoginRequestDTO): Response<LoginResponseDTO>

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
    suspend fun getAccountInfo(): Response<AccountInfoDTO>

    @PATCH("account")
    suspend fun updateAccount(@Body account: AccountInfoDTO): Response<MessageResponseDTO>

    @DELETE("account")
    suspend fun deleteAccount(): Response<MessageResponseDTO>

    @PATCH("account/password")
    suspend fun changePassword(@Body data: PasswordChangeRequestDTO): Response<MessageResponseDTO>

    @POST("account/2fa/setup")
    suspend fun setup2FA(): Response<TotpResponseDTO>

    @GET("account/2fa/status")
    suspend fun get2FAStatus(): Response<BooleanResponseDTO>

    @POST("account/2fa/disable")
    suspend fun disable2FA(): Response<MessageResponseDTO>
}