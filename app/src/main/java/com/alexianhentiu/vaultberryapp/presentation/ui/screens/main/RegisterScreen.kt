package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexianhentiu.vaultberryapp.presentation.activity.MainActivity
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RegisterState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun RegisterScreen(
    navManager: NavigationManager
) {
    val activity = LocalActivity.current as MainActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val registerViewModel: RegisterViewModel = hiltViewModel()
    val registerState by registerViewModel.registerState.collectAsState()

    when (registerState) {
        is RegisterState.Idle -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navManager.navigate(NavRoute.SETTINGS) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    RegisterForm(
                        navManager = navManager,
                        onRegisterClicked = { email, password, firstName, lastName ->
                            registerViewModel.register(email, password, firstName, lastName)
                        },
                        inputValidator = registerViewModel.inputValidator,
                        passwordEvaluator = registerViewModel.passwordEvaluator
                    )
                }
            }
        }

        is RegisterState.Loading -> {
            LoadingScreen()
        }

        is RegisterState.Success -> {
            val clipboardManager = LocalClipboardManager.current
            val recoveryPassword = (registerState as RegisterState.Success).recoveryPassword
            InfoDialog(
                title = "Account registration successful",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation. " +
                        "Make sure to write it down!",
                onDismissRequest = {
                    clipboardManager.setText(AnnotatedString(recoveryPassword))
                    registerViewModel.resetState()
                    navManager.navigate(NavRoute.LOGIN)
                }
            )
        }

        is RegisterState.Error -> {
            val errorMessage = (registerState as RegisterState.Error).info.message
            ErrorDialog(
                onConfirm = { registerViewModel.resetState() },
                title = "Registration Error",
                message = errorMessage
            )
        }
    }
}