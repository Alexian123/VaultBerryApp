package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.SaveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveSettingUseCase: SaveSettingUseCase,
    observeSettingUseCase: ObserveSettingUseCase
) : ViewModel() {

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

    fun setUseSystemTheme(useSystemTheme: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(AppSettings.USE_SYSTEM_THEME, useSystemTheme)) {
                is UseCaseResult.Success -> {
                    Log.d("SettingsViewModel", "setUseSystemTheme: $useSystemTheme")
                }

                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
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
                }
            }
        }
    }
}