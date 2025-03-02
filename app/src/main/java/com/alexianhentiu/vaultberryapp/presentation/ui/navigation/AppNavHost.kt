package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.base.AccountScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.base.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.base.RecoveryScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.base.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.base.SettingsScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.base.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RecoveryViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(loginViewModel, navController)
        }
        composable("register") {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(registerViewModel, navController)
        }
        composable("recovery") {
            val recoveryViewModel: RecoveryViewModel = hiltViewModel()
            RecoveryScreen(recoveryViewModel, navController)
        }
        composable("vault") {
            val vaultViewModel: VaultViewModel = hiltViewModel()
            VaultScreen(vaultViewModel, navController)
        }
        composable("account") {
            val accountViewModel: AccountViewModel = hiltViewModel()
            AccountScreen(accountViewModel, navController)
        }
        composable("settings") {
            SettingsScreen(settingsViewModel, navController)
        }
    }
}