package com.alexianhentiu.vaultberryapp.data.remote

import com.alexianhentiu.vaultberryapp.data.remote.model.AccountInfoDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.PasswordPairDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.UserDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.VaultEntryPreviewDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.LoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.PasswordChangeRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.RecoveryLoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.request.VaultSearchRequestDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.LoginResponseDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.response.TotpResponseDTO
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.request.RecoveryLoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.LoginResponse
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelConverter @Inject constructor() {

    fun recoveryLoginRequestToDTO(request: RecoveryLoginRequest): RecoveryLoginRequestDTO {
        return RecoveryLoginRequestDTO(
            email = request.email,
            recoveryPassword = request.recoveryPassword,
            otp = request.otp
        )
    }

    fun loginRequestToDTO(loginRequest: LoginRequest): LoginRequestDTO {
        return LoginRequestDTO(
            email = loginRequest.email,
            clientMessage = loginRequest.clientMessage,
            totpCode = loginRequest.totpCode
        )
    }

    fun loginResponseFromDTO(loginResponseDTO: LoginResponseDTO): LoginResponse {
        if (loginResponseDTO.keyChain == null) {
            return LoginResponse(
                serverMessage = loginResponseDTO.serverMessage,
                keyChain = null
            )
        }
        return LoginResponse(
            serverMessage = loginResponseDTO.serverMessage,
            keyChain = keyChainFromDTO(loginResponseDTO.keyChain)
        )
    }

    fun totpResponseFromDTO(totpResponseDTO: TotpResponseDTO): TotpResponse {
        return TotpResponse(
            provisioningUri = totpResponseDTO.provisioningUri
        )
    }

    fun messageResponseFromDTO(messageResponseDTO: MessageResponseDTO): MessageResponse {
        return MessageResponse(
            message = messageResponseDTO.message
        )
    }

    fun userToDTO(user: User): UserDTO {
        return UserDTO(
            accountInfoDTO = accountInfoToDTO(user.accountInfo),
            keyChainDTO = keyChainToDTO(user.keyChain),
            passwordPairDTO = passwordPairToDTO(user.passwordPair),
            noActivation = user.noActivation
        )
    }

    fun accountInfoToDTO(accountInfo: AccountInfo): AccountInfoDTO {
        return AccountInfoDTO(
            email = accountInfo.email,
            firstName = accountInfo.firstName,
            lastName = accountInfo.lastName
        )
    }

    fun accountInfoFromDTO(accountInfoDTO: AccountInfoDTO): AccountInfo {
        return AccountInfo(
            email = accountInfoDTO.email,
            firstName = accountInfoDTO.firstName,
            lastName = accountInfoDTO.lastName
        )
    }

    fun passwordPairToDTO(passwordPair: PasswordPair): PasswordPairDTO {
        return PasswordPairDTO(
            regularPassword = passwordPair.regularPassword,
            recoveryPassword = passwordPair.recoveryPassword
        )
    }

    fun keyChainToDTO(keyChain: KeyChain): KeyChainDTO {
        return KeyChainDTO(
            salt = keyChain.salt,
            vaultKey = keyChain.vaultKey,
            recoveryKey = keyChain.recoveryKey
        )
    }

    fun keyChainFromDTO(keyChainDTO: KeyChainDTO): KeyChain {
        return KeyChain(
            salt = keyChainDTO.salt,
            vaultKey = keyChainDTO.vaultKey,
            recoveryKey = keyChainDTO.recoveryKey
        )
    }

    fun passwordChangeRequestToDTO(
        passwordChangeRequest: PasswordChangeRequest
    ): PasswordChangeRequestDTO {
        return PasswordChangeRequestDTO(
            passwordPair = passwordPairToDTO(passwordChangeRequest.passwordPair),
            keyChain = keyChainToDTO(passwordChangeRequest.keyChain),
            reEncrypt = passwordChangeRequest.reEncrypt
        )
    }

    fun encryptedVaultEntryToDTO(encryptedVaultEntry: EncryptedVaultEntry): EncryptedVaultEntryDTO {
        return EncryptedVaultEntryDTO(
            title = encryptedVaultEntry.title,
            url = encryptedVaultEntry.url,
            encryptedUsername = encryptedVaultEntry.encryptedUsername,
            encryptedPassword = encryptedVaultEntry.encryptedPassword,
            notes = encryptedVaultEntry.notes
        )
    }

    fun encryptedVaultEntryFromDTO(
        encryptedVaultEntryDTO: EncryptedVaultEntryDTO
    ): EncryptedVaultEntry {
        return EncryptedVaultEntry(
            title = encryptedVaultEntryDTO.title,
            url = encryptedVaultEntryDTO.url,
            encryptedUsername = encryptedVaultEntryDTO.encryptedUsername,
            encryptedPassword = encryptedVaultEntryDTO.encryptedPassword,
            notes = encryptedVaultEntryDTO.notes
        )
    }

    fun vaultEntryPreviewFromDTO(
        vaultEntryPreviewDTO: VaultEntryPreviewDTO
    ): VaultEntryPreview {
        return VaultEntryPreview(
            id = vaultEntryPreviewDTO.id,
            title = vaultEntryPreviewDTO.title
        )
    }

    fun keywordsToVaultSearchRequest(keywords: List<String>): VaultSearchRequestDTO {
        return VaultSearchRequestDTO(keywords)
    }
}