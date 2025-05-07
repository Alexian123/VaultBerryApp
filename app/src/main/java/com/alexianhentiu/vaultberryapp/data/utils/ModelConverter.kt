package com.alexianhentiu.vaultberryapp.data.utils

import com.alexianhentiu.vaultberryapp.data.model.entity.AccountInfoDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.model.response.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.request.PasswordChangeRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.PasswordPairDTO
import com.alexianhentiu.vaultberryapp.data.model.response.TotpResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.UserDTO
import com.alexianhentiu.vaultberryapp.data.model.entity.VaultEntryPreviewDTO
import com.alexianhentiu.vaultberryapp.data.model.request.LoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.request.RecoveryLoginRequestDTO
import com.alexianhentiu.vaultberryapp.data.model.response.LoginResponseDTO
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.entity.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.model.entity.User
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.request.RecoveryLoginRequest
import com.alexianhentiu.vaultberryapp.domain.model.response.LoginResponse

class ModelConverter {

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
            passwordPairDTO = passwordPairToDTO(user.passwordPair)
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
            keyChain = keyChainToDTO(passwordChangeRequest.keyChain)
        )
    }

    fun encryptedVaultEntryToDTO(encryptedVaultEntry: EncryptedVaultEntry): EncryptedVaultEntryDTO {
        return EncryptedVaultEntryDTO(
            lastModified = encryptedVaultEntry.lastModified,
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
            lastModified = encryptedVaultEntryDTO.lastModified,
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
}