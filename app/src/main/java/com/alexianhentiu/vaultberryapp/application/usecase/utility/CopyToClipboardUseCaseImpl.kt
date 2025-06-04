package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.CopyToClipboardUseCase

class CopyToClipboardUseCaseImpl(
    private val clipboardHandler: ClipboardHandler
) : CopyToClipboardUseCase {

    override operator fun invoke(text: String, label: String): UseCaseResult<Unit> {
        try {
            clipboardHandler.copyToClipboard(text, label)
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