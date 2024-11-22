package com.alexianhentiu.vaultberryapp.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.alexianhentiu.vaultberryapp.presentation.ui.login.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaultBerryAppTheme {
                LoginScreen(viewModel = loginViewModel)
            }
        }
    }
}