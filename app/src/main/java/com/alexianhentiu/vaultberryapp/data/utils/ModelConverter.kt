package com.alexianhentiu.vaultberryapp.data.utils

import com.alexianhentiu.vaultberryapp.data.model.AccountDTO
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.KeyChainDTO
import com.alexianhentiu.vaultberryapp.data.model.LoginCredentialsDTO
import com.alexianhentiu.vaultberryapp.data.model.MessageResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.PasswordPairDTO
import com.alexianhentiu.vaultberryapp.data.model.TotpResponseDTO
import com.alexianhentiu.vaultberryapp.data.model.UserDTO
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.model.User

class ModelConverter {

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
            accountDTO = accountToDTO(user.account),
            keyChainDTO = keyChainToDTO(user.keyChain),
            passwordPairDTO = passwordPairToDTO(user.passwordPair)
        )
    }

    fun accountToDTO(account: Account): AccountDTO {
        return AccountDTO(
            email = account.email,
            firstName = account.firstName,
            lastName = account.lastName
        )
    }

    fun accountFromDTO(accountDTO: AccountDTO): Account {
        return Account(
            email = accountDTO.email,
            firstName = accountDTO.firstName,
            lastName = accountDTO.lastName
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

    fun loginCredentialsToDTO(loginCredentials: LoginCredentials): LoginCredentialsDTO {
        return LoginCredentialsDTO(
            email = loginCredentials.email,
            password = loginCredentials.password,
            token = loginCredentials.token
        )
    }

    fun encryptedVaultEntryToDTO(encryptedVaultEntry: EncryptedVaultEntry): EncryptedVaultEntryDTO {
        return EncryptedVaultEntryDTO(
            timestamp = encryptedVaultEntry.timestamp,
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
            timestamp = encryptedVaultEntryDTO.timestamp,
            title = encryptedVaultEntryDTO.title,
            url = encryptedVaultEntryDTO.url,
            encryptedUsername = encryptedVaultEntryDTO.encryptedUsername,
            encryptedPassword = encryptedVaultEntryDTO.encryptedPassword,
            notes = encryptedVaultEntryDTO.notes
        )
    }
}