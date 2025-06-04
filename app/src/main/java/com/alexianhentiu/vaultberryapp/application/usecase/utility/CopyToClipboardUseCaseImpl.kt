package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class CopyToClipboardUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider,
    private val clipboardHandler: ClipboardHandler
) : CopyToClipboardUseCase {

    override operator fun invoke(text: String, label: String): UseCaseResult<Unit> {
        try {
            clipboardHandler.copyToClipboard(text, label)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.COPY_TO_CLIPBOARD_FAILURE,
                source = stringResourceProvider.getString(R.string.clipboard_handler_error_source),
                message = e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }
}