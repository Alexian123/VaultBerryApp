package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.AccountScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.RecoveryScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.SettingsScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navManager = NavigationManager(navController)

    NavHost(navController = navController, startDestination = NavRoute.LOGIN.path) {
        composable(NavRoute.LOGIN.path) {
            LoginScreen(navManager)
        }
        composable(NavRoute.REGISTER.path) {
            RegisterScreen(navManager)
        }
        composable(NavRoute.RECOVERY.path) {
            RecoveryScreen(navManager)
        }
        composable(NavRoute.VAULT.path) {
            VaultScreen(navManager)
        }
        composable(NavRoute.ACCOUNT.path) {
            AccountScreen(navManager)
        }
        composable(NavRoute.SETTINGS.path) {
            SettingsScreen(navManager)
        }
    }
}