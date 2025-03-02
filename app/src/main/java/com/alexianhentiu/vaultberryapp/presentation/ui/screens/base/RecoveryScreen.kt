package com.alexianhentiu.vaultberryapp.presentation.ui.screens.base

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
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ChangePasswordForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.OTPRequestForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RecoveryLoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.extra.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RecoveryViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.RecoveryState

@Composable
fun RecoveryScreen(
    viewModel: RecoveryViewModel,
    navController: NavController,
) {
    val recoveryState by viewModel.recoveryState.collectAsState()

    when (recoveryState) {
        is RecoveryState.Error -> {
            val errorMessage = (recoveryState as RecoveryState.Error).message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Recovery Error",
                message = errorMessage
            )
        }

        is RecoveryState.Loading -> {
            LoadingScreen()
        }

        is RecoveryState.Idle -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    OTPRequestForm(
                        onContinueClicked = { email -> viewModel.requestOTP(email) },
                        onCancelClicked = { navController.navigate("login") },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
        }

        is RecoveryState.OTPRequested -> {
            val email = (recoveryState as RecoveryState.OTPRequested).email
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    RecoveryLoginForm(
                        onContinueClicked = { otp, recoveryPassword ->
                            viewModel.recoveryLogin(email, otp, recoveryPassword)
                        },
                        onCancelClicked = {
                            viewModel.resetState()
                            navController.navigate("login")
                        },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
        }

        is RecoveryState.LoggedIn -> {
            val decryptedKey = (recoveryState as RecoveryState.LoggedIn).decryptedKey
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    ChangePasswordForm(
                        onChangePassword = { newPassword ->
                            viewModel.resetPassword(decryptedKey, newPassword)
                        },
                        inputValidator = viewModel.inputValidator,
                        textFieldType = TextFieldType.REGULAR
                    )
                }
            }
        }

        is RecoveryState.PasswordReset -> {
            val clipboardManager = LocalClipboardManager.current
            val recoveryPassword = (recoveryState as RecoveryState.PasswordReset).newRecoveryPassword
            InfoDialog(
                title = "Password reset successfully",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    clipboardManager.setText(AnnotatedString(recoveryPassword))
                    viewModel.logout()
                    navController.navigate("login")
                }
            )
        }
    }
}