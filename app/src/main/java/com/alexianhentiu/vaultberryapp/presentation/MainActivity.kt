package com.alexianhentiu.vaultberryapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaultBerryAppTheme {
                Text("Hello, Jetpack Compose!")
            }
        }
    }
}