package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import android.util.Log
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class ChangePasswordUseCase(
    private val accountRepository: AccountRepository,
    private val generatePasswordPairUseCase: GeneratePasswordPairUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase,
    private val decryptKeyUseCase: DecryptKeyUseCase,
    private val reEncryptVaultUseCase: ReEncryptVaultUseCase
) {
    suspend operator fun invoke(
        decryptedKey: ByteArray,
        newPassword: String,
        reEncrypt: Boolean = false
    ): UseCaseResult<String> {

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
        if (changeResult is APIResult.Error) {
            return UseCaseResult.Error(
                ErrorType.EXTERNAL,
                changeResult.source,
                changeResult.message
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
            val msg = (reEncryptResult as UseCaseResult.Success).data
            Log.d("ChangePasswordUseCase", msg.message)
        }

        return UseCaseResult.Success(newPasswordPair.recoveryPassword)
    }
}