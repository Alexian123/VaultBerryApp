package com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.AppSettings
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.SaveSettingUseCase
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
                flowOf(AppSettings.USE_SYSTEM_THEME.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = AppSettings.USE_SYSTEM_THEME.defaultValue
        )

    val darkTheme: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.DARK_THEME)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                flowOf(AppSettings.DARK_THEME.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = AppSettings.DARK_THEME.defaultValue
        )

    val debugMode: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.DEBUG_MODE)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                flowOf(AppSettings.DEBUG_MODE.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = AppSettings.DEBUG_MODE.defaultValue
        )

    val savedEmail: StateFlow<String> =
        when (val result = observeSettingUseCase(AppSettings.SAVED_EMAIL)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                flowOf(AppSettings.SAVED_EMAIL.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = AppSettings.SAVED_EMAIL.defaultValue
        )

    val rememberEmail: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.REMEMBER_EMAIL)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                flowOf(AppSettings.REMEMBER_EMAIL.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = AppSettings.REMEMBER_EMAIL.defaultValue
        )

    val biometricEnabled: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(AppSettings.BIOMETRIC_ENABLED)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                flowOf(AppSettings.BIOMETRIC_ENABLED.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = AppSettings.BIOMETRIC_ENABLED.defaultValue
        )

    fun setUseSystemTheme(useSystemTheme: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.USE_SYSTEM_THEME, useSystemTheme)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }

    fun setDarkTheme(useDarkTheme: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.DARK_THEME, useDarkTheme)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }

    fun setDebugMode(debugMode: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.DEBUG_MODE, debugMode)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }

    fun setSavedEmail(email: String) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.SAVED_EMAIL, email)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }

    fun setRememberEmail(rememberEmail: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.REMEMBER_EMAIL, rememberEmail)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }

    fun setBiometricEnabled(biometricEnabled: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.BIOMETRIC_ENABLED, biometricEnabled)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }
}