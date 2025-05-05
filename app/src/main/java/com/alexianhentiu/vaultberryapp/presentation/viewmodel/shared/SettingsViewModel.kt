package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val useSystemThemeKey = booleanPreferencesKey("use_system_theme")
    private val darkThemeKey = booleanPreferencesKey("dark_theme")

    private val _useSystemTheme = MutableStateFlow(false)
    val useSystemTheme: StateFlow<Boolean> = _useSystemTheme

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme

    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[useSystemThemeKey] != false
            }.collect { useSystemTheme ->
                _useSystemTheme.value = useSystemTheme
            }
        }
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[darkThemeKey] == true
            }.collect { darkTheme ->
                _darkTheme.value = darkTheme
            }
        }
    }

    fun setUseSystemTheme(useSystemTheme: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[useSystemThemeKey] = useSystemTheme
            }
            Log.d("SettingsViewModel", "setUseSystemTheme: $useSystemTheme")
        }
    }

    fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[darkThemeKey] = darkTheme
            }
            Log.d("SettingsViewModel", "setDarkTheme: $darkTheme")
        }
    }
}