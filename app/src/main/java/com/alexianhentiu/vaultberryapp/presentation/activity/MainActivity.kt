package com.alexianhentiu.vaultberryapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexianhentiu.vaultberryapp.presentation.ui.navigation.AppNavHost
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // TODO: Implement error dialog
    // TODO: Add title to login/register pages
    // TODO: Implement statistics & suggestions for entry password security
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaultBerryAppTheme {
                AppNavHost()
            }
        }
    }
}