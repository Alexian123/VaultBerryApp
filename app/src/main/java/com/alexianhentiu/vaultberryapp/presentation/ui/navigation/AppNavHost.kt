package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.AboutScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.AccountScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.PasswordGeneratorScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.RecoveryScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.SettingsScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val activity = LocalActivity.current as ComponentActivity
    val sessionViewModel: SessionViewModel = hiltViewModel(activity)
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)
    val settingsViewModel: SettingsViewModel = hiltViewModel(activity)

    NavHost(navController = navController, startDestination = NavRoute.LOGIN.path) {
        composable(NavRoute.LOGIN.path) {
            LoginScreen(navController, sessionViewModel, utilityViewModel, settingsViewModel)
        }
        composable(NavRoute.REGISTER.path) {
            RegisterScreen(navController, utilityViewModel)
        }
        composable(NavRoute.RECOVERY.path) {
            RecoveryScreen(navController, sessionViewModel, utilityViewModel)
        }
        composable(NavRoute.VAULT.path) {
            VaultScreen(navController, sessionViewModel, utilityViewModel)
        }
        composable(NavRoute.ACCOUNT.path) {
            AccountScreen(navController, sessionViewModel, utilityViewModel)
        }
        composable(NavRoute.PASSWORD_GENERATOR.path) {
            PasswordGeneratorScreen(navController, utilityViewModel)
        }
        composable(NavRoute.SETTINGS.path) {
            SettingsScreen(navController, settingsViewModel)
        }
        composable(NavRoute.ABOUT.path) {
            AboutScreen(navController)
        }
    }
}