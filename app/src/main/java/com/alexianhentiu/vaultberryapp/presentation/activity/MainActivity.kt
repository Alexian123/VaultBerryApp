package com.alexianhentiu.vaultberryapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.enums.AppTheme
import com.alexianhentiu.vaultberryapp.presentation.ui.navigation.AppNavHost
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
    TODO: High Priority Tasks
    1. Obtain the new key and send it back to the vault screen after changing the password
    2. Confirm dialogs for account updates
    3. More generic return type for use cases (not APIResult)
    4. Exception handling for security use cases
    5. One time prompt with the newly generated recovery password
    6. Recovery screen
    7. Handle back button/gesture in the vault screen
    8. Integrate VPN connection
*/

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val useSystemTheme = settingsViewModel.useSystemTheme.collectAsState()
            val darkTheme = settingsViewModel.darkTheme.collectAsState()

            // set current theme based on user preferences
            val currentTheme =
                if (useSystemTheme.value) remember { mutableStateOf(AppTheme.FOLLOW_SYSTEM) }
                else if (darkTheme.value) remember { mutableStateOf(AppTheme.DARK) }
                else remember { mutableStateOf(AppTheme.LIGHT) }

            VaultBerryAppTheme(appTheme = currentTheme.value) {
                AppNavHost()
            }
        }
    }
}