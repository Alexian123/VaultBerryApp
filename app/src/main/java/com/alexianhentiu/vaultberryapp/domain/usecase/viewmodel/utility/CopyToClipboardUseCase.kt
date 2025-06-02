package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import android.content.ClipData
import android.content.ClipboardManager
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class CopyToClipboardUseCase(
    private val clipboardManager: ClipboardManager
) {
    operator fun invoke(text: String, label: String = ""): UseCaseResult<Unit> {
        try {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.COPY_TO_CLIPBOARD_FAILURE,
                source = "Clipboard Manager",
                message = e.message ?: "Unknown error"
            )
        }
    }
}