package com.alexianhentiu.vaultberryapp.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import com.alexianhentiu.vaultberryapp.presentation.ui.handlers.biometric.BiometricPromptHandler
import com.alexianhentiu.vaultberryapp.presentation.ui.handlers.SettingsErrorHandler
import com.alexianhentiu.vaultberryapp.presentation.ui.handlers.UtilityErrorHandler
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.AppTheme
import com.alexianhentiu.vaultberryapp.presentation.ui.navigation.AppNavHost
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.BiometricViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
    TODO:
        - Fix Clean Code violations in BiometricViewModel & BiometricPromptHandler
        - Cleanup code & add comments
*/

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val utilityViewModel: UtilityViewModel by viewModels()
    private val biometricViewModel: BiometricViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val useSystemTheme = settingsViewModel.useSystemTheme.collectAsState()
            val darkTheme = settingsViewModel.darkTheme.collectAsState()

            // set current theme based on user preferences
            val currentTheme =
                if (useSystemTheme.value) remember { mutableStateOf(AppTheme.FOLLOW_SYSTEM) }
                else if (darkTheme.value) remember { mutableStateOf(AppTheme.DARK) }
                else remember { mutableStateOf(AppTheme.LIGHT) }

            VaultBerryAppTheme(appTheme = currentTheme.value) {
                AppNavHost()
                BiometricPromptHandler(
                    fragmentActivity = this,
                    biometricViewModel = biometricViewModel
                )
                UtilityErrorHandler(utilityViewModel)
                SettingsErrorHandler(settingsViewModel)
            }
        }
    }
}