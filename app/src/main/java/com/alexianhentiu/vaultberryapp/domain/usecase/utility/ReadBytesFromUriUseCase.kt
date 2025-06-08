package com.alexianhentiu.vaultberryapp.domain.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import java.net.URI

interface ReadBytesFromUriUseCase {
    suspend operator fun invoke(uri: URI): UseCaseResult<ByteArray>
}