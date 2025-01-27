package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.AccountScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AuthViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.MotionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val vaultViewModel: VaultViewModel = hiltViewModel()
    val accountViewModel: AccountViewModel = hiltViewModel()
    val motionViewModel: MotionViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(authViewModel, vaultViewModel, navController)
        }
        composable("register") {
            RegisterScreen(registerViewModel, navController)
        }
        composable("vault") {
            VaultScreen(
                vaultViewModel,
                authViewModel,
                accountViewModel,
                motionViewModel,
                navController
            )
        }
        composable("account") {
            AccountScreen(vaultViewModel, authViewModel, accountViewModel, navController)
        }
        composable("settings") {
            // TODO: Implement settings screen
        }
    }
}