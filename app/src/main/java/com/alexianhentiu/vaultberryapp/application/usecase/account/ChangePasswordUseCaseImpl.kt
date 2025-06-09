package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class ChangePasswordUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val generatePasswordPairUseCase: GeneratePasswordPairUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase,
    private val decryptKeyUseCase: DecryptKeyUseCase,
    private val reEncryptVaultUseCase: ReEncryptVaultUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ChangePasswordUseCase {

    override suspend operator fun invoke(
        decryptedKey: ByteArray,
        newPassword: String,
        reEncrypt: Boolean
    ): UseCaseResult<String> {
        try {
            val newPasswordPairResult = generatePasswordPairUseCase(newPassword)
            if (newPasswordPairResult is UseCaseResult.Error) {
                return newPasswordPairResult
            }
            val newPasswordPair = (newPasswordPairResult as UseCaseResult.Success).data

            val generateKeyChainResult = generateKeyChainUseCase(
                newPasswordPair.regularPassword,
                newPasswordPair.recoveryPassword,
                decryptedKey
            )
            if (generateKeyChainResult is UseCaseResult.Error) {
                return generateKeyChainResult
            }
            val newKeyChain = (generateKeyChainResult as UseCaseResult.Success).data

            // Update the password & keychain
            val passwordChangeRequest = PasswordChangeRequest(
                passwordPair = newPasswordPair,
                keyChain = newKeyChain,
                reEncrypt = reEncrypt
            )
            val changeResult = accountRepository.changePassword(passwordChangeRequest)
            if (changeResult is ApiResult.Error) {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        changeResult.source,
                        changeResult.message
                    )
                )
            }

            // Re-encrypt vault if requested
            if (reEncrypt) {
                val decryptKeyResult = decryptKeyUseCase(
                    newPassword,
                    newKeyChain.salt,
                    newKeyChain.vaultKey
                )
                if (decryptKeyResult is UseCaseResult.Error) {
                    return decryptKeyResult
                }
                val newDecryptedKey = (decryptKeyResult as UseCaseResult.Success).data

                val reEncryptResult = reEncryptVaultUseCase(decryptedKey, newDecryptedKey)
                if (reEncryptResult is UseCaseResult.Error) {
                    return reEncryptResult
                }
            }

            return UseCaseResult.Success(newPasswordPair.recoveryPassword)
        } catch (e: Exception) {
           return  UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}