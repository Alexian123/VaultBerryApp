package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.SaveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.setting.BooleanSetting
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

    private val useSystemThemeDef = BooleanSetting("use_system_theme", true)
    private val darkThemeDef = BooleanSetting("dark_theme", false)
    private val debugModeDef = BooleanSetting("debug_mode", false)

    val useSystemTheme: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(useSystemThemeDef)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(useSystemThemeDef.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = useSystemThemeDef.defaultValue
        )

    val darkTheme: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(darkThemeDef)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(darkThemeDef.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = darkThemeDef.defaultValue
        )

    val debugMode: StateFlow<Boolean> =
        when (val result = observeSettingUseCase(debugModeDef)) {
            is UseCaseResult.Success -> result.data
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                flowOf(debugModeDef.defaultValue)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = debugModeDef.defaultValue
        )

    fun setUseSystemTheme(useSystemTheme: Boolean) {
        viewModelScope.launch {
            when (val result = saveSettingUseCase(useSystemThemeDef, useSystemTheme)) {
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
            when (val result = saveSettingUseCase(darkThemeDef, useDarkTheme)) {
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
            when (val result = saveSettingUseCase(debugModeDef, debugMode)) {
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