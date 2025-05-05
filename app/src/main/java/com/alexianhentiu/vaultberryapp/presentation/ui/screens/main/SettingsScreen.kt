package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.bars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.SwitchSettingItem
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel? = null,
    navManager: NavigationManager,

) {
    val key = navManager.retrieveVaultKey()

    var useSystemTheme by remember { mutableStateOf(viewModel?.useSystemTheme?.value != false) }
    var darkTheme by remember { mutableStateOf(viewModel?.darkTheme?.value == true) }

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
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
            ) {
                SwitchSettingItem(
                    text = "Use system theme",
                    checked = useSystemTheme,
                    onCheckedChange = {
                        useSystemTheme = it
                        viewModel?.setUseSystemTheme(useSystemTheme)
                    }
                )
                SwitchSettingItem(
                    enabled = !useSystemTheme,
                    text = "Dark theme",
                    checked = darkTheme,
                    onCheckedChange = {
                        darkTheme = it
                        viewModel?.setDarkTheme(darkTheme)
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
        viewModel = null,
        navManager = NavigationManager(NavController(LocalContext.current))
    )
}