package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.LoginViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RecoveryViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.VaultViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navManager = NavigationManager(navController)

    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = NavRoute.LOGIN.path) {
        composable(NavRoute.LOGIN.path) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(loginViewModel, navManager)
        }
        composable(NavRoute.REGISTER.path) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(registerViewModel, navManager)
        }
        composable(NavRoute.RECOVERY.path) {
            val recoveryViewModel: RecoveryViewModel = hiltViewModel()
            RecoveryScreen(recoveryViewModel, navManager)
        }
        composable(NavRoute.VAULT.path) {
            val vaultViewModel: VaultViewModel = hiltViewModel()
            VaultScreen(vaultViewModel, navManager)
        }
        composable(NavRoute.ACCOUNT.path) {
            val accountViewModel: AccountViewModel = hiltViewModel()
            AccountScreen(accountViewModel, navManager)
        }
        composable(NavRoute.SETTINGS.path) {
            SettingsScreen(settingsViewModel, navManager)
        }
    }
}