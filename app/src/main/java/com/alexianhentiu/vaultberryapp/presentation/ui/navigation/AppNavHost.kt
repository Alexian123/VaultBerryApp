package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.AccountScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.PasswordGeneratorScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.RecoveryScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.SettingsScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.LOGIN.path) {
        composable(NavRoute.LOGIN.path) {
            LoginScreen(navController)
        }
        composable(NavRoute.REGISTER.path) {
            RegisterScreen(navController)
        }
        composable(NavRoute.RECOVERY.path) {
            RecoveryScreen(navController)
        }
        composable(NavRoute.VAULT.path) {
            VaultScreen(navController)
        }
        composable(NavRoute.ACCOUNT.path) {
            AccountScreen(navController)
        }
        composable(NavRoute.PASSWORD_GENERATOR.path) {
            PasswordGeneratorScreen(navController)
        }
        composable(NavRoute.SETTINGS.path) {
            SettingsScreen(navController)
        }
    }
}