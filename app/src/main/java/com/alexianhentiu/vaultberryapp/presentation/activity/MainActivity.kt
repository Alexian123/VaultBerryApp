package com.alexianhentiu.vaultberryapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexianhentiu.vaultberryapp.presentation.ui.navigation.AppNavHost
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaultBerryAppTheme {
                AppNavHost()
            }
        }
    }
}