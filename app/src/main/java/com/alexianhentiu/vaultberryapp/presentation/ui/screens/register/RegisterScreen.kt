package com.alexianhentiu.vaultberryapp.presentation.ui.screens.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.ui.common.viewmodels.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.viewmodels.UtilityViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    utilityViewModel: UtilityViewModel,
    settingsViewModel: SettingsViewModel,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val screenState by registerViewModel.registerScreenState.collectAsState()
    val recoveryPassword by registerViewModel.recoveryPasswordEvent.collectAsState(initial = "")

    val isDebugMode by settingsViewModel.debugMode.collectAsState()

    var showRecoveryPasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(recoveryPassword) {
        if (recoveryPassword.isNotEmpty()) {
            showRecoveryPasswordDialog = true
        }
    }

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
                            registerViewModel.register(
                                email,
                                password,
                                firstName,
                                lastName,
                                isDebugMode
                            )
                        },
                        validator = {
                            utilityViewModel.getValidatorFunction(it, isDebugMode)
                        },
                        evaluatePasswordStrength = utilityViewModel::evalPasswordStrength
                    )
                }

                is RegisterScreenState.Loading -> {
                    LoadingAnimationDialog()
                }

                is RegisterScreenState.Success -> {
                    if (showRecoveryPasswordDialog) {
                        InfoDialog(
                            title = stringResource(R.string.register_screen_success_title),
                            message = stringResource(R.string.recovery_password_message_p1) +
                                    "\n$recoveryPassword" +
                                    stringResource(R.string.recovery_password_message_p2) +
                                    stringResource(R.string.recovery_password_message_p3),
                            onDismissRequest = {
                                utilityViewModel.copyToClipboard(recoveryPassword)
                                registerViewModel.resetState()
                                navController.navigate(NavRoute.LOGIN.path)
                            }
                        )
                    }
                }

                is RegisterScreenState.Error -> {
                    val errorInfo = (screenState as RegisterScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { registerViewModel.resetState() },
                        errorInfo = errorInfo,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail,
                                appName = utilityViewModel.getAppInfo(AppInfo.APP_NAME),
                                appVersionName = utilityViewModel.getAppInfo(
                                    AppInfo.VERSION_NAME
                                ),
                            )
                        }
                    )
                }
            }
        }
    }
}