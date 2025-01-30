package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.vault.ReEncryptAllEntriesUseCase

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
    ): APIResult<String> {
        when (val account = accountRepository.getAccount()) {
            is APIResult.Success -> {
                val newAccount = account.data.copy(password = newPassword)
                val recoveryPassword = generatePasswordUseCase()
                val newKeyChain = generateKeyChainUseCase(newPassword, recoveryPassword)
                val newDecryptedKey = decryptKeyUseCase(
                    password = newPassword,
                    salt = newKeyChain.salt,
                    encryptedKey = newKeyChain.vaultKey
                )
                when (val updateResult = accountRepository.updateAccount(newAccount)) {
                    is APIResult.Success -> {
                        when (val reEncryptResult = reEncryptAllEntriesUseCase(
                            oldKey = decryptedKey,
                            newKey = newDecryptedKey
                        )) {
                            is APIResult.Success -> {
                                return when (
                                    val updateKeyResult = accountRepository.updateKeyChain(
                                        keychain = newKeyChain
                                    )
                                ) {
                                    is APIResult.Success -> {
                                        APIResult.Success(recoveryPassword)
                                    }

                                    is APIResult.Error -> {
                                        APIResult.Error(updateKeyResult.message)
                                    }
                                }
                            }

                            is APIResult.Error -> {
                                return APIResult.Error(reEncryptResult.message)
                            }
                        }
                    }

                    is APIResult.Error -> {
                        return APIResult.Error(updateResult.message)
                    }
                }
            }

            is APIResult.Error -> {
                return APIResult.Error(account.message)
            }
        }
    }
}