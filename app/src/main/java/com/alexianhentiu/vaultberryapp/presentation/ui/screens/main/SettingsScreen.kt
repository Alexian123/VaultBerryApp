package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.activity.MainActivity
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.SwitchSettingItem
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel

@Composable
fun SettingsScreen(
    navManager: NavigationManager,
) {
    val activity = LocalActivity.current as MainActivity
    val settingsViewModel: SettingsViewModel = hiltViewModel(activity)

    val key = navManager.retrieveVaultKey()

    val useSystemTheme by settingsViewModel.useSystemTheme.collectAsState()
    val darkTheme by settingsViewModel.darkTheme.collectAsState()
    val debugMode by settingsViewModel.debugMode.collectAsState()

    BackHandler(enabled = true) {
        if (key != null) {
            // Settings was accessed from vault
            navManager.navigateWithVaultKey(NavRoute.VAULT, key)
        } else {
            // Settings was accessed from elsewhere
            navManager.goBack()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                onBackClick = {
                    if (key != null) {
                        navManager.navigateWithVaultKey(NavRoute.VAULT, key)
                    } else {
                        navManager.goBack()
                    }
                },
                title = "Settings"
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SwitchSettingItem(
                    text = "Use system theme",
                    checked = useSystemTheme,
                    onCheckedChange = {
                        settingsViewModel.setUseSystemTheme(it)
                    }
                )
                SwitchSettingItem(
                    enabled = !useSystemTheme,
                    text = "Dark theme",
                    checked = darkTheme,
                    onCheckedChange = {
                        settingsViewModel.setDarkTheme(it)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Advanced (restart required)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                SwitchSettingItem(
                    text = "Debug mode",
                    checked = debugMode,
                    onCheckedChange = {
                        settingsViewModel.setDebugMode(it)
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    SettingsScreen(
        navManager = NavigationManager(NavController(LocalContext.current))
    )
}