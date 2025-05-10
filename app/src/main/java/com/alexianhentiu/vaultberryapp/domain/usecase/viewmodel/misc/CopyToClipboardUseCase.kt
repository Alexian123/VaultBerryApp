package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.misc

import android.content.ClipData
import android.content.ClipboardManager
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class CopyToClipboardUseCase(
    private val clipboardManager: ClipboardManager
) {
    operator fun invoke(text: String, label: String = ""): ActionResult<Unit> {
        try {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
            return ActionResult.Success(Unit)
        } catch (e: Exception) {
            return ActionResult.Error(
                type = ErrorType.INTERNAL,
                source = "Clipboard",
                message = "Copy to clipboard failed: ${e.message}"
            )
        }
    }
}