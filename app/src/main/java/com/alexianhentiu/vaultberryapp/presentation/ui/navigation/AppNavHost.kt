package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AuthViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.MotionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(authViewModel, navController)
        }
        composable("register") {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(registerViewModel, navController)
        }
        composable("vault") {
            val vaultViewModel: VaultViewModel = hiltViewModel()
            val motionViewModel: MotionViewModel = hiltViewModel()
            VaultScreen(vaultViewModel, authViewModel, motionViewModel, navController)
        }
    }
}