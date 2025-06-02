package com.alexianhentiu.vaultberryapp.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.BiometricPromptHandler
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.AppTheme
import com.alexianhentiu.vaultberryapp.presentation.ui.navigation.AppNavHost
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.BiometricViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
    TODO:
        - Confirm password on register
        - Email verification & account activation for register/email change
        - Add option to set the backend ip and import SSL certificate
        - Cleanup code & add comments
*/

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
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
            }
        }
    }
}