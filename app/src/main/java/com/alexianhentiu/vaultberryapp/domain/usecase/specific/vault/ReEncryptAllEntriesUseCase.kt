package com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault

import android.util.Log
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ReEncryptAllEntriesUseCase(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        oldKey: DecryptedKey,
        newKey: DecryptedKey
    ): ActionResult<MessageResponse> {
        return when (val result = vaultRepository.getEntries()) {
            is APIResult.Success -> {
                reEncryptEntries(result.data, oldKey, newKey)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }

    private suspend fun reEncryptEntries(
        entries: List<EncryptedVaultEntry>,
        oldKey: DecryptedKey,
        newKey: DecryptedKey
    ): ActionResult<MessageResponse> {
        for (entry in entries) {
            val decryptedEntryResult = decryptVaultEntryUseCase(entry, oldKey)
            if (decryptedEntryResult is ActionResult.Error) {
                return decryptedEntryResult
            }
            val decryptedEntry = (decryptedEntryResult as ActionResult.Success).data

            val encryptedEntryResult = encryptVaultEntryUseCase(decryptedEntry, newKey)
            if (encryptedEntryResult is ActionResult.Error) {
                return encryptedEntryResult
            }
            val newEncryptedEntry = (encryptedEntryResult as ActionResult.Success).data

            val updateResult = vaultRepository.updateEntry(newEncryptedEntry)
            if (updateResult is APIResult.Error) {
                return ActionResult.Error(
                    ErrorType.EXTERNAL,
                    updateResult.source,
                    updateResult.message
                )
            }
            val updateMessageResponse = (updateResult as APIResult.Success).data
            Log.d("ReEncryptAllEntriesUseCase", updateMessageResponse.message)
        }
        return ActionResult.Success(MessageResponse("All entries re-encrypted successfully"))
    }
}