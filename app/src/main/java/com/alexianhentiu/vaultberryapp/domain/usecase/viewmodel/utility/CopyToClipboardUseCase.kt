package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import android.content.ClipData
import android.content.ClipboardManager
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class CopyToClipboardUseCase(
    private val clipboardManager: ClipboardManager
) {
    operator fun invoke(text: String, label: String = ""): UseCaseResult<Unit> {
        try {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.INTERNAL,
                source = "Clipboard",
                message = "Copy to clipboard failed: ${e.message}"
            )
        }
    }
}