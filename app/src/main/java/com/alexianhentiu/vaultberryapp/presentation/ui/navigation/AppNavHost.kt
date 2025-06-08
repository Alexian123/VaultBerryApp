package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.about.AboutScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.account.AccountScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.login.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.passwordGenerator.PasswordGeneratorScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.recovery.RecoveryScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.register.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.settings.SettingsScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.BiometricViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.apiConfig.ApiConfigScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val activity = LocalActivity.current as ComponentActivity
    val sessionViewModel: SessionViewModel = hiltViewModel(activity)
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)
    val settingsViewModel: SettingsViewModel = hiltViewModel(activity)
    val biometricViewModel: BiometricViewModel = hiltViewModel(activity)

    NavHost(navController = navController, startDestination = NavRoute.LOGIN.path) {
        composable(NavRoute.LOGIN.path) {
            LoginScreen(
                navController,
                sessionViewModel,
                utilityViewModel,
                settingsViewModel,
                biometricViewModel
            )
        }
        composable(NavRoute.REGISTER.path) {
            RegisterScreen(navController, utilityViewModel, settingsViewModel)
        }
        composable(NavRoute.RECOVERY.path) {
            RecoveryScreen(navController, sessionViewModel, utilityViewModel, settingsViewModel)
        }
        composable(NavRoute.VAULT.path) {
            VaultScreen(navController, sessionViewModel, utilityViewModel, settingsViewModel)
        }
        composable(NavRoute.ACCOUNT.path) {
            AccountScreen(navController, sessionViewModel, utilityViewModel, settingsViewModel)
        }
        composable(NavRoute.PASSWORD_GENERATOR.path) {
            PasswordGeneratorScreen(navController, utilityViewModel)
        }
        composable(NavRoute.SETTINGS.path) {
            SettingsScreen(navController, settingsViewModel, biometricViewModel)
        }
        composable(NavRoute.ABOUT.path) {
            AboutScreen(navController)
        }
        composable(NavRoute.API_CONFIG.path) {
            ApiConfigScreen(navController, utilityViewModel)
        }
    }
}