package com.alexianhentiu.vaultberryapp.domain.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface CopyToClipboardUseCase {
    operator fun invoke(text: String, label: String = ""): UseCaseResult<Unit>
}