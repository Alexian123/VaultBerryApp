package com.alexianhentiu.vaultberryapp.presentation.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.auth.LoginScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.auth.RegisterScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault.VaultScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(loginViewModel, navController)
        }
        composable("register") {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(registerViewModel, navController)
        }
        composable("vault") {
            val vaultViewModel: VaultViewModel = hiltViewModel()
            VaultScreen(vaultViewModel, navController)
        }
    }
}