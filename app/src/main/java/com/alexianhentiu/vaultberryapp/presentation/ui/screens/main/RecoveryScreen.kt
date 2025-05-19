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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ChangePasswordForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.OTPRequestForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RecoveryLoginForm
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RecoveryViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RecoveryScreenState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun RecoveryScreen(
    navController: NavHostController
) {
    val activity = LocalActivity.current as ComponentActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)
    val sessionViewModel: SessionViewModel = hiltViewModel(activity)

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val recoveryViewModel: RecoveryViewModel = hiltViewModel()
    val screenState by recoveryViewModel.recoveryScreenState.collectAsState()

    when (screenState) {
        is RecoveryScreenState.Error -> {
            val errorMessage = (screenState as RecoveryScreenState.Error).info.message
            ErrorDialog(
                onConfirm = { recoveryViewModel.resetState() },
                title = "Recovery Error",
                message = errorMessage
            )
        }

        is RecoveryScreenState.Loading -> {
            LoadingScreen()
        }

        is RecoveryScreenState.Idle -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    OTPRequestForm(
                        onContinueClicked = { email -> recoveryViewModel.requestOTP(email) },
                        onCancelClicked = { navController.navigate(NavRoute.LOGIN.path) },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }
            }
        }

        is RecoveryScreenState.OTPRequested -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
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
            }
        }

        is RecoveryScreenState.LoggedIn -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
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
            }
        }

        is RecoveryScreenState.PasswordReset -> {
            val recoveryPassword = recoveryViewModel.tempRecoveryPassword.collectAsState()
            InfoDialog(
                title = "Password reset successfully",
                message = "Your new recovery password is: \"${recoveryPassword.value}\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    utilityViewModel.copyToClipboard(recoveryPassword.value)
                    sessionViewModel.logout()
                    recoveryViewModel.clearData()
                    navController.navigate(NavRoute.LOGIN.path)
                }
            )
        }
    }
}