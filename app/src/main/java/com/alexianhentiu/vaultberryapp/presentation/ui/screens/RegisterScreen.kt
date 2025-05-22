package com.alexianhentiu.vaultberryapp.presentation.ui.screens

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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RegisterScreenState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    utilityViewModel: UtilityViewModel,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val inputValidator by utilityViewModel.inputValidator.collectAsState()
    val screenState by registerViewModel.registerScreenState.collectAsState()

    Scaffold(
        topBar = {
            AuthTopBar(
                onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) },
                titleText = "Register"
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

            when (screenState) {
                is RegisterScreenState.Idle -> {

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

                is RegisterScreenState.Loading -> {
                    LoadingAnimationDialog()
                }

                is RegisterScreenState.Success -> {
                    val recoveryPassword =
                        (screenState as RegisterScreenState.Success).recoveryPassword
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
    }
}