package com.alexianhentiu.vaultberryapp.presentation.ui.screens

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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ChangePasswordForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.OTPRequestForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RecoveryLoginForm
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.helper.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RecoveryViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RecoveryScreenState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun RecoveryScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    utilityViewModel: UtilityViewModel,
    recoveryViewModel: RecoveryViewModel = hiltViewModel()
) {
    val inputValidator by utilityViewModel.inputValidator.collectAsState()
    val screenState by recoveryViewModel.recoveryScreenState.collectAsState()
    val recoveryPassword by recoveryViewModel.recoveryPasswordEvent.collectAsState(initial = "")

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
                titleText = "Account Recovery"
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

            when (screenState) {
                is RecoveryScreenState.Loading -> {
                    LoadingAnimationDialog()
                }

                is RecoveryScreenState.Idle -> {

                    OTPRequestForm(
                        onContinueClicked = { email -> recoveryViewModel.requestOTP(email) },
                        onCancelClicked = { navController.navigate(NavRoute.LOGIN.path) },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }

                is RecoveryScreenState.OTPRequested -> {
                    RecoveryLoginForm(
                        onContinueClicked = { recoveryPassword, otp ->
                            recoveryViewModel.recoveryLogin(null, recoveryPassword, otp)
                        },
                        onCancelClicked = {
                            recoveryViewModel.resetState()
                        },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }

                is RecoveryScreenState.LoggedIn -> {
                    ChangePasswordForm(
                        onChangePassword = { newPassword, reEncrypt ->
                            recoveryViewModel.resetPassword(newPassword, reEncrypt)
                        },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        },
                        evaluatePasswordStrength = utilityViewModel::evalPasswordStrength,
                        textFieldType = TextFieldType.REGULAR
                    )
                }

                is RecoveryScreenState.PasswordReset -> {
                    if (showRecoveryPasswordDialog) {
                        InfoDialog(
                            title = stringResource(R.string.recovery_screen_success_title),
                            message = stringResource(R.string.recovery_password_message_p1) +
                                    "\n$recoveryPassword" +
                                    stringResource(R.string.recovery_password_message_p2) +
                                    stringResource(R.string.recovery_password_message_p3),
                            onDismissRequest = {
                                utilityViewModel.copyToClipboard(recoveryPassword)
                                sessionViewModel.logout()
                                recoveryViewModel.clearData()
                                navController.navigate(NavRoute.LOGIN.path)
                            }
                        )
                    }
                }

                is RecoveryScreenState.Error -> {
                    val errorInfo = (screenState as RecoveryScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { recoveryViewModel.resetState() },
                        errorInfo = errorInfo,
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