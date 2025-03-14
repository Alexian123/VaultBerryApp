package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.vault.ReEncryptAllEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ChangePasswordUseCase(
    private val accountRepository: AccountRepository,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val decryptKeyUseCase: DecryptKeyUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase,
    private val reEncryptAllEntriesUseCase: ReEncryptAllEntriesUseCase
) {
    suspend operator fun invoke(
        decryptedKey: DecryptedKey,
        newPassword: String
    ): ActionResult<String> {
        val generatePasswordResult = generatePasswordUseCase()
        if (generatePasswordResult is ActionResult.Error) {
            return generatePasswordResult
        }
        val recoveryPassword = (generatePasswordResult as ActionResult.Success).data

        val generateKeyChainResult = generateKeyChainUseCase(newPassword, recoveryPassword)
        if (generateKeyChainResult is ActionResult.Error) {
            return generateKeyChainResult
        }
        val newKeyChain = (generateKeyChainResult as ActionResult.Success).data

        val decryptKeyResult = decryptKeyUseCase(
            password = newPassword,
            salt = newKeyChain.salt,
            encryptedKey = newKeyChain.vaultKey
        )
        if (decryptKeyResult is ActionResult.Error) {
            return decryptKeyResult
        }
        val newDecryptedKey = (decryptKeyResult as ActionResult.Success).data

        val newPasswordPair = PasswordPair(newPassword, recoveryPassword)
        when (val changeResult = accountRepository.changePassword(newPasswordPair)) {
            is APIResult.Success -> {
                val reEncryptResult = reEncryptAllEntriesUseCase(
                    oldKey = decryptedKey,
                    newKey = newDecryptedKey
                )
                if (reEncryptResult is ActionResult.Error) {
                    return reEncryptResult
                }

                return when (val updateKeyResult = accountRepository.updateKeyChain(
                        keychain = newKeyChain
                    )
                ) {
                    is APIResult.Success -> {
                        ActionResult.Success(recoveryPassword)
                    }

                    is APIResult.Error -> {
                        ActionResult.Error(
                            ErrorType.EXTERNAL,
                            updateKeyResult.source,
                            updateKeyResult.message
                        )
                    }
                }
            }

            is APIResult.Error -> {
                return ActionResult.Error(
                    ErrorType.EXTERNAL,
                    changeResult.source,
                    changeResult.message
                )
            }
        }
    }
}