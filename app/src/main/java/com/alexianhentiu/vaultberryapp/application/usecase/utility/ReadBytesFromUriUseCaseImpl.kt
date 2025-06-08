package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.ReadBytesFromUriUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import com.alexianhentiu.vaultberryapp.domain.utils.UriStreamProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI

class ReadBytesFromUriUseCaseImpl(
    private val uriStreamProvider: UriStreamProvider,
    private val stringResourceProvider: StringResourceProvider
) : ReadBytesFromUriUseCase {

    override suspend fun invoke(uri: URI): UseCaseResult<ByteArray> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = uriStreamProvider.openInputStream(uri)
                    ?: return@withContext UseCaseResult.Error(
                        ErrorInfo(
                            type = ErrorType.OPEN_URI_INPUT_STREAM_FAILURE,
                            source = stringResourceProvider.getString(
                                R.string.error_source_uri_stream_provider
                            ),
                            message = stringResourceProvider.getString(
                                R.string.error_message_uri_stream_provider
                            )
                        )
                    )
                val bytes = inputStream.readBytes()
                inputStream.close()
                UseCaseResult.Success(bytes)
            } catch (e: Exception) {
                UseCaseResult.Error(
                    ErrorInfo(
                        type = ErrorType.READ_BYTES_FROM_URI_FAILURE,
                        source = stringResourceProvider.getString(
                            R.string.error_source_uri_stream_provider
                        ),
                        message = e.message
                            ?: stringResourceProvider.getString(R.string.unknown_error)
                    )
                )
            }
        }
    }

}