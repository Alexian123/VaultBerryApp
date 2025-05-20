package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ValidatedFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.presentation.utils.autofill.AutofillEntry
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AutofillState
import com.alexianhentiu.vaultberryapp.presentation.utils.state.SessionState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.AutofillViewModel

@Composable
fun AutofillScreen(
    keywords: List<String>,
    onSuccess: (List<AutofillEntry>) -> Unit,
) {
    val activity = LocalActivity.current as ComponentActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)
    val sessionViewModel: SessionViewModel = hiltViewModel(activity)
    val settingsViewModel: SettingsViewModel = hiltViewModel(activity)

    val savedEmail by settingsViewModel.savedEmail.collectAsState()
    val rememberEmail by settingsViewModel.rememberEmail.collectAsState()

    val screenState by sessionViewModel.sessionState.collectAsState()

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val autofillViewModel: AutofillViewModel = hiltViewModel()

    var finishedSearching by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {}

    when (screenState) {
        is SessionState.LoggedOut -> {
            var email by remember(savedEmail, rememberEmail) {
                mutableStateOf(if (rememberEmail) savedEmail else "")
            }
            var password by remember { mutableStateOf("") }

            var isEmailValid by remember(email) {
                mutableStateOf(
                    inputValidator?.getValidatorFunction(
                        ValidatedFieldType.EMAIL
                    )?.invoke(email) == true
                )
            }

            var isPasswordValid by remember { mutableStateOf(false) }

            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        ValidatedTextField(
                            label = "Email",
                            initialText = email,
                            onInputChange = { newEmail, valid ->
                                email = newEmail
                                isEmailValid = valid
                            },
                            isValid = {
                                val func = inputValidator?.getValidatorFunction(
                                    ValidatedFieldType.EMAIL
                                )
                                func?.invoke(it) == true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PasswordField(
                            onPasswordChange = { newPassword, valid ->
                                password = newPassword
                                isPasswordValid = valid
                            },
                            isValid = {
                                val func = inputValidator?.getValidatorFunction(
                                    ValidatedFieldType.PASSWORD
                                )
                                func?.invoke(it) == true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { sessionViewModel.login(email, password) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEmailValid && isPasswordValid && !finishedSearching
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }

        is SessionState.Loading -> {
            LoadingScreen()
        }

        is SessionState.TwoFactorRequired -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    Verify2FAForm(
                        onContinueClicked = { code ->
                            sessionViewModel.login(null, null, code)
                        },
                        onCancelClicked = {
                            autofillViewModel.resetState()
                            sessionViewModel.resetState()
                        },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }
            }
        }

        is SessionState.LoggedIn -> {
            val autofillState by autofillViewModel.state.collectAsState()
            when (autofillState) {
                is AutofillState.Idle -> {
                    val decryptedKey by sessionViewModel.decryptedKey.collectAsState()
                    autofillViewModel.searchVaultEntries(keywords, decryptedKey)
                }

                is AutofillState.Loading -> {
                    LoadingScreen()
                }

                is AutofillState.Success -> {
                    val suggestions by autofillViewModel.autofillSuggestions.collectAsState()
                    finishedSearching = true
                    onSuccess(suggestions)
                    autofillViewModel.resetState()
                    sessionViewModel.logout()
                }

                is AutofillState.Error -> {
                    val errorMessage = (autofillState as AutofillState.Error).info.message
                    ErrorDialog(
                        onConfirm = {
                            autofillViewModel.resetState()
                        },
                        title = "Autofill Error",
                        message = errorMessage
                    )
                }
            }
        }

        is SessionState.Error -> {
            val errorMessage = (screenState as SessionState.Error).info.message
            ErrorDialog(
                onConfirm = {
                    sessionViewModel.resetState()
                },
                title = "Autofill Error",
                message = errorMessage
            )
        }
    }
}