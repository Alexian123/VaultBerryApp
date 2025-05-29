package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.helper.launchErrorReportEmailIntent
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
                titleText = stringResource(R.string.register_screen_title)
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
                        title = stringResource(R.string.register_screen_success_title),
                        message = stringResource(R.string.recovery_password_message_p1) +
                                " \"$recoveryPassword\". " +
                                stringResource(R.string.recovery_password_message_p2) +
                                stringResource(R.string.recovery_password_message_p3),
                        onDismissRequest = {
                            utilityViewModel.copyToClipboard(recoveryPassword)
                            registerViewModel.resetState()
                            navController.navigate(NavRoute.LOGIN.path)
                        }
                    )
                }

                is RegisterScreenState.Error -> {
                    val errorInfo = (screenState as RegisterScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { registerViewModel.resetState() },
                        title = "${errorInfo.type.name} ERROR",
                        source = errorInfo.source,
                        message = errorInfo.message,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail
                            )
                        }
                    )
                }
            }
        }
    }
}