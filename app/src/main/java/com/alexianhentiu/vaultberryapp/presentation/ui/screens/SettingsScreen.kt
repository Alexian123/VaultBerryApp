package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.SettingsForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel
import androidx.core.net.toUri
import com.alexianhentiu.vaultberryapp.R

@Composable
fun SettingsScreen(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    val useSystemTheme by settingsViewModel.useSystemTheme.collectAsState()
    val darkTheme by settingsViewModel.darkTheme.collectAsState()
    val debugMode by settingsViewModel.debugMode.collectAsState()

    var goToSystemSettings by remember { mutableStateOf(false) }

    if (goToSystemSettings) {
        goToSystemSettings = false
        val context = LocalContext.current
        val intent = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE).apply {
            data = "package:${context.packageName}".toUri()
        }
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = stringResource(R.string.settings_screen_title)
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            SettingsForm(
                useSystemTheme = useSystemTheme,
                darkTheme = darkTheme,
                debugMode = debugMode,
                onUseSystemThemeChange = { settingsViewModel.setUseSystemTheme(it) },
                onDarkThemeChange = { settingsViewModel.setDarkTheme(it) },
                onDebugModeChange = { settingsViewModel.setDebugMode(it) },
                onAutofillActivation = {
                    goToSystemSettings = true
                }
            )
        }
    }
}