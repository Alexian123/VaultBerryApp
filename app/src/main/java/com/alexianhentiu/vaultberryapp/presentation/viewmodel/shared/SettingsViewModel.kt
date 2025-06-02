package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.SaveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.settings.AppSettings
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveSettingUseCase: SaveSettingUseCase,
    observeSettingUseCase: ObserveSettingUseCase
) : ViewModel() {

    private val _errorInfo = MutableSharedFlow<ErrorInfo>()
    val errorInfo: SharedFlow<ErrorInfo> = _errorInfo.asSharedFlow()

    val useSystemTheme: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.USE_SYSTEM_THEME)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(AppSettings.USE_SYSTEM_THEME.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings.USE_SYSTEM_THEME.defaultValue
        )

    val darkTheme: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.DARK_THEME)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(AppSettings.DARK_THEME.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings.DARK_THEME.defaultValue
        )

    val debugMode: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.DEBUG_MODE)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(AppSettings.DEBUG_MODE.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings.DEBUG_MODE.defaultValue
        )

    val savedEmail: StateFlow<String> =
        when (val result = observeSettingUseCase(AppSettings.SAVED_EMAIL)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(AppSettings.SAVED_EMAIL.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings.SAVED_EMAIL.defaultValue
        )

    val rememberEmail: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.REMEMBER_EMAIL)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(AppSettings.REMEMBER_EMAIL.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings.REMEMBER_EMAIL.defaultValue
        )

    val biometricEnabled: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.BIOMETRIC_ENABLED)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(AppSettings.BIOMETRIC_ENABLED.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings.BIOMETRIC_ENABLED.defaultValue
        )

    fun setUseSystemTheme(useSystemTheme: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.USE_SYSTEM_THEME, useSystemTheme)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setUseSystemTheme: $useSystemTheme")
                }

                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
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

    fun setDarkTheme(useDarkTheme: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.DARK_THEME, useDarkTheme)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setUseDarkTheme: $useDarkTheme")
                }
                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
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

    fun setDebugMode(debugMode: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.DEBUG_MODE, debugMode)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setDebugMode: $debugMode")
                }
                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
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

    fun setSavedEmail(email: String) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.SAVED_EMAIL, email)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setSavedEmail: $email")
                }
                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
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

    fun setRememberEmail(rememberEmail: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.REMEMBER_EMAIL, rememberEmail)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setRememberEmail: $rememberEmail")
                }
                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
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

    fun setBiometricEnabled(biometricEnabled: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.BIOMETRIC_ENABLED, biometricEnabled)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setBiometricEnabled: $biometricEnabled")
                }
                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
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
}