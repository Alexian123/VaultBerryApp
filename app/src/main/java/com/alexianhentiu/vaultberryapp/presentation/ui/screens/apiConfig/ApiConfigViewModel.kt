package com.alexianhentiu.vaultberryapp.presentation.ui.screens.apiConfig

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.ClearApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.GetApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.GetApiUrlUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.PingApiUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.SetApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.SetApiUrlUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.ReadBytesFromUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class ApiConfigViewModel @Inject constructor(
    private val getApiUrlUseCase: GetApiUrlUseCase,
    private val setApiUrlUseCase: SetApiUrlUseCase,
    private val pingApiUseCase: PingApiUseCase,
    private val getApiCertificateUseCase: GetApiCertificateUseCase,
    private val setApiCertificateUseCase: SetApiCertificateUseCase,
    private val clearApiCertificateUseCase: ClearApiCertificateUseCase,
    private val readBytesFromUriUseCase: ReadBytesFromUriUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<ApiConfigScreenState>(ApiConfigScreenState.Idle)
    val screenState: StateFlow<ApiConfigScreenState> = _screenState

    private val _pingState = MutableStateFlow<PingState>(PingState.Idle)
    val pingState: StateFlow<PingState> = _pingState

    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url

    private val _certificate = MutableStateFlow<ByteArray?>(null)
    val certificate: StateFlow<ByteArray?> = _certificate

    fun loadExistingData() {
        viewModelScope.launch {
            _screenState.value = ApiConfigScreenState.Loading
            when (val urlResult = getApiUrlUseCase()) {
                is UseCaseResult.Success -> {
                    when (val certificateResult = getApiCertificateUseCase()) {
                        is UseCaseResult.Success -> {
                            _screenState.value = ApiConfigScreenState.Ready
                            _url.value = urlResult.data
                            _certificate.value = certificateResult.data
                        }
                        is UseCaseResult.Error -> {
                            _screenState.value = ApiConfigScreenState.Error(
                                ErrorInfo(
                                    type = certificateResult.type,
                                    source = certificateResult.source,
                                    message = certificateResult.message
                                )
                            )
                        }
                    }
                }

                is UseCaseResult.Error -> {
                    _screenState.value = ApiConfigScreenState.Error(
                        ErrorInfo(
                            type = urlResult.type,
                            source = urlResult.source,
                            message = urlResult.message
                        )
                    )
                }
            }
        }
    }

    fun saveApiUrl(url: String) {
        viewModelScope.launch {
            _screenState.value = ApiConfigScreenState.Loading
            when (val result = setApiUrlUseCase(url)) {
                is UseCaseResult.Success -> {
                    _screenState.value = ApiConfigScreenState.Success
                    _url.value = url
                }
                is UseCaseResult.Error -> {
                    _screenState.value = ApiConfigScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun pingApi(host: String, port: Int) {
        viewModelScope.launch {
            _pingState.value = PingState.Loading
            when (val result = pingApiUseCase(host, port, 5000)) {
                is UseCaseResult.Success -> {
                    _pingState.value = PingState.Success(host, port)
                }
                is UseCaseResult.Error -> {
                    _pingState.value = PingState.Failure(result.message)
                }
            }
        }
    }

    fun saveApiCertificate(certificateURI: Uri) {
        viewModelScope.launch {
            _screenState.value = ApiConfigScreenState.Loading
            val javaURI = URI(certificateURI.toString())
            when (val readResult = readBytesFromUriUseCase(javaURI)) {
                is UseCaseResult.Success -> {
                    when (val saveResult = setApiCertificateUseCase(readResult.data)) {
                        is UseCaseResult.Success -> {
                            _screenState.value = ApiConfigScreenState.Success
                            _certificate.value = readResult.data
                        }
                        is UseCaseResult.Error -> {
                            _screenState.value = ApiConfigScreenState.Error(
                                ErrorInfo(
                                    type = saveResult.type,
                                    source = saveResult.source,
                                    message = saveResult.message
                                )
                            )
                        }
                    }

                }

                is UseCaseResult.Error -> {
                    _screenState.value = ApiConfigScreenState.Error(
                        ErrorInfo(
                            type = readResult.type,
                            source = readResult.source,
                            message = readResult.message
                        )
                    )
                }
            }
        }
    }

    fun clearApiCertificate() {
        viewModelScope.launch {
            _screenState.value = ApiConfigScreenState.Loading
            when (val result = clearApiCertificateUseCase()) {
                is UseCaseResult.Success -> {
                    _screenState.value = ApiConfigScreenState.Success
                    _certificate.value = null
                }
                is UseCaseResult.Error -> {
                    _screenState.value = ApiConfigScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun resetState() {
        _screenState.value = ApiConfigScreenState.Idle
        _url.value = ""
        _certificate.value = null
        resetPingState()
    }

    fun setReadyState() {
        _screenState.value = ApiConfigScreenState.Ready
    }

    fun resetPingState() {
        _pingState.value = PingState.Idle

    }
}