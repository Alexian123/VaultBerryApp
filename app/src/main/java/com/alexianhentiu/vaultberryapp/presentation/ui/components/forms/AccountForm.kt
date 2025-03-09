package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.ExpandableSectionItem

@Composable
fun AccountForm(
    account: Account,
    is2FAEnabled: Boolean,
    onSaveInfo: (String?, String?, String?) -> Unit, // Pass null for unchanged values
    onChangePassword: (String) -> Unit,
    onEnable2FA: () -> Unit,
    onDisable2FA: () -> Unit,
    onDeleteAccount: () -> Unit,
    inputValidator: InputValidator
) {
    var isInfoExpanded by remember { mutableStateOf(false) }
    var isPasswordExpanded by remember { mutableStateOf(false) }
    var is2FAExpanded by remember { mutableStateOf(false) }
    var isDangerousExpanded by remember { mutableStateOf(false) }

    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    if (showConfirmDeleteDialog) {
        ConfirmActionDialog(
            title = "Delete Account",
            message = "Are you sure you want to delete your account?",
            onDismissRequest = { showConfirmDeleteDialog = false },
            onSubmit = {
                if (it) {
                    onDeleteAccount()
                    showConfirmDeleteDialog = false
                }
            }
        )
    }

    Column {
        ExpandableSectionItem(
            title = "Name and Email",
            onExpand = { isInfoExpanded = !isInfoExpanded }
        )
        AnimatedVisibility(visible = isInfoExpanded) {
            ChangeAccountInfoForm(
                account = account,
                onSaveInfo = onSaveInfo
            )
        }
        ExpandableSectionItem(
            title = "Password",
            onExpand = { isPasswordExpanded = !isPasswordExpanded }
        )
        AnimatedVisibility(visible = isPasswordExpanded) {
            ChangePasswordForm(
                onChangePassword = onChangePassword,
                inputValidator = inputValidator
            )
        }
        ExpandableSectionItem(
            title = "Two-Factor Authentication",
            onExpand = { is2FAExpanded = !is2FAExpanded }
        )
        AnimatedVisibility(visible = is2FAExpanded) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (is2FAEnabled) {
                    Text("2FA is enabled")
                    Button(onClick = onDisable2FA) {
                        Text("Disable 2FA")
                    }
                } else {
                    Text("2FA is not enabled")
                    Button(onClick = onEnable2FA) {
                        Text("Enable 2FA")
                    }
                }
            }
        }
        ExpandableSectionItem(
            title = "Dangerous",
            onExpand = { isDangerousExpanded = !isDangerousExpanded }
        )
        AnimatedVisibility(
            visible = isDangerousExpanded,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Button(
                onClick = { showConfirmDeleteDialog = true }
            ) {
                Text("Delete Account")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountFormPreview() {
    AccountForm(
        account = Account(
            firstName = "John",
            lastName = "Doe",
            email = "william.henry.harrison@example-pet-store.com"
        ),
        is2FAEnabled = false,
        onSaveInfo = { _, _, _ -> },
        onChangePassword = {},
        onEnable2FA = {},
        onDisable2FA = {},
        onDeleteAccount = {},
        inputValidator = InputValidator()
    )
}