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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ChangePasswordForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.OTPRequestForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RecoveryLoginForm
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.helper.launchErrorReportEmailIntent
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
                        textFieldType = TextFieldType.REGULAR
                    )
                }

                is RecoveryScreenState.PasswordReset -> {
                    val recoveryPassword = recoveryViewModel.tempRecoveryPassword.collectAsState()
                    InfoDialog(
                        title = "Password reset successfully",
                        message = "Your new recovery password is: \"${recoveryPassword.value}\". " +
                                "Make sure to save it in a safe place." +
                                "It will be copied into the clipboard upon confirmation.",
                        onDismissRequest = {
                            utilityViewModel.copyToClipboard(recoveryPassword.value)
                            sessionViewModel.logout()
                            recoveryViewModel.clearData()
                            navController.navigate(NavRoute.LOGIN.path)
                        }
                    )
                }

                is RecoveryScreenState.Error -> {
                    val errorInfo = (screenState as RecoveryScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { recoveryViewModel.resetState() },
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