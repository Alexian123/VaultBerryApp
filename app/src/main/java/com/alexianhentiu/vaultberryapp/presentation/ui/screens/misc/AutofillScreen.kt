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
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.AutofillViewModel

@Composable
fun AutofillScreen(
    keywords: List<String>,
    onSuccess: (List<AutofillEntry>) -> Unit,
) {
    val activity = LocalActivity.current as ComponentActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val autofillViewModel: AutofillViewModel = hiltViewModel()
    val autofillState by autofillViewModel.autofillState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    fun resetFields() {
        email = ""
        password = ""
        isEmailValid = false
        isPasswordValid = false
    }

    BackHandler(enabled = true) {}

    when (autofillState) {
        is AutofillState.Idle -> {
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
                            onClick = { autofillViewModel.login(email, password) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEmailValid && isPasswordValid
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }

        is AutofillState.Loading -> {
            LoadingScreen()
        }

        is AutofillState.Verify2FA -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    Verify2FAForm(
                        onContinueClicked = { code ->
                            autofillViewModel.login(email, password, code)
                        },
                        onCancelClicked = {
                            autofillViewModel.resetState()
                            resetFields()
                        },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }
            }
        }

        is AutofillState.LoggedIn -> {
            autofillViewModel.searchVaultEntries(keywords)
            resetFields()
        }

        is AutofillState.Success -> {
            val autofillSuggestions by autofillViewModel.autofillSuggestions.collectAsState()
            onSuccess(autofillSuggestions)
            autofillViewModel.logout()
            resetFields()
        }

        is AutofillState.Error -> {
            val errorMessage = (autofillState as AutofillState.Error).info.message
            ErrorDialog(
                onConfirm = { autofillViewModel.resetState() },
                title = "Autofill Error",
                message = errorMessage
            )
        }
    }
}