package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RegisterScreenState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val activity = LocalActivity.current as ComponentActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val registerViewModel: RegisterViewModel = hiltViewModel()
    val screenState by registerViewModel.registerScreenState.collectAsState()

    when (screenState) {
        is RegisterScreenState.Idle -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    RegisterForm(
                        navController = navController,
                        onRegisterClicked = { email, password, firstName, lastName ->
                            registerViewModel.register(email, password, firstName, lastName)
                        },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }
            }
        }

        is RegisterScreenState.Loading -> {
            LoadingScreen()
        }

        is RegisterScreenState.Success -> {
            val recoveryPassword = (screenState as RegisterScreenState.Success).recoveryPassword
            InfoDialog(
                title = "Account registration successful",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation. " +
                        "Make sure to write it down!",
                onDismissRequest = {
                    utilityViewModel.copyToClipboard(recoveryPassword)
                    registerViewModel.resetState()
                    navController.navigate(NavRoute.LOGIN.path)
                }
            )
        }

        is RegisterScreenState.Error -> {
            val errorMessage = (screenState as RegisterScreenState.Error).info.message
            ErrorDialog(
                onConfirm = { registerViewModel.resetState() },
                title = "Registration Error",
                message = errorMessage
            )
        }
    }
}