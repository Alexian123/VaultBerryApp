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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ChangePasswordForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.OTPRequestForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RecoveryLoginForm
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RecoveryViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RecoveryState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun RecoveryScreen(
    navManager: NavigationManager,
) {
    val activity = LocalActivity.current as MainActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val recoveryViewModel: RecoveryViewModel = hiltViewModel()
    val recoveryState by recoveryViewModel.recoveryState.collectAsState()

    when (recoveryState) {
        is RecoveryState.Error -> {
            val errorMessage = (recoveryState as RecoveryState.Error).info.message
            ErrorDialog(
                onConfirm = { recoveryViewModel.resetState() },
                title = "Recovery Error",
                message = errorMessage
            )
        }

        is RecoveryState.Loading -> {
            LoadingScreen()
        }

        is RecoveryState.Idle -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navManager.navigate(NavRoute.SETTINGS) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    OTPRequestForm(
                        onContinueClicked = { email -> recoveryViewModel.requestOTP(email) },
                        onCancelClicked = { navManager.navigate(NavRoute.LOGIN) },
                        inputValidator = recoveryViewModel.inputValidator
                    )
                }
            }
        }

        is RecoveryState.OTPRequested -> {
            val email = (recoveryState as RecoveryState.OTPRequested).email
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    RecoveryLoginForm(
                        onContinueClicked = { recoveryPassword, otp ->
                            recoveryViewModel.recoveryLogin(email, recoveryPassword, otp)
                        },
                        onCancelClicked = {
                            recoveryViewModel.resetState()
                            navManager.navigate(NavRoute.LOGIN)
                        },
                        inputValidator = recoveryViewModel.inputValidator,
                        passwordEvaluator = recoveryViewModel.passwordEvaluator
                    )
                }
            }
        }

        is RecoveryState.LoggedIn -> {
            val decryptedKey = (recoveryState as RecoveryState.LoggedIn).decryptedKey
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    ChangePasswordForm(
                        onChangePassword = { newPassword, reEncrypt ->
                            recoveryViewModel.resetPassword(decryptedKey, newPassword, reEncrypt)
                        },
                        inputValidator = recoveryViewModel.inputValidator,
                        passwordEvaluator = recoveryViewModel.passwordEvaluator,
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
                    recoveryViewModel.logout()
                    navManager.navigate(NavRoute.LOGIN)
                }
            )
        }
    }
}