package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.DebugValidator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.ExpandableSectionItem

@Composable
fun AccountForm(
    accountInfo: AccountInfo,
    is2FAEnabled: Boolean,
    onSaveInfo: (String?, String?, String?) -> Unit, // Pass null for unchanged values
    onChangePassword: (String, Boolean) -> Unit,
    onEnable2FA: () -> Unit,
    onDisable2FA: () -> Unit,
    onDeleteAccount: () -> Unit,
    inputValidator: InputValidator,
    passwordEvaluator: PasswordEvaluator
) {
    var isInfoExpanded by remember { mutableStateOf(false) }
    var isSecurityExpanded by remember { mutableStateOf(false) }
    var isDangerousExpanded by remember { mutableStateOf(false) }

    var newPassword by remember { mutableStateOf("") }
    var shouldReEncrypt by remember { mutableStateOf(false) }

    var showConfirmPasswordChangeDialog by remember { mutableStateOf(false) }
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

    if (showConfirmPasswordChangeDialog) {
        ConfirmActionDialog(
            title = "Change Password",
            message = "Are you sure you want to change your password?",
            onDismissRequest = { showConfirmPasswordChangeDialog = false },
            onSubmit = {
                if (it) {
                    onChangePassword(newPassword, shouldReEncrypt)
                    showConfirmPasswordChangeDialog = false
                }
            }
        )
    }

    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        item {
            ExpandableSectionItem(
                title = "Information",
                onExpand = { isInfoExpanded = !isInfoExpanded }
            )
            AnimatedVisibility(visible = isInfoExpanded) {
                ChangeAccountInfoForm(
                    accountInfo = accountInfo,
                    onSaveInfo = onSaveInfo
                )
            }
        }
        item {
            ExpandableSectionItem(
                title = "Security",
                onExpand = { isSecurityExpanded = !isSecurityExpanded }
            )
            AnimatedVisibility(visible = isSecurityExpanded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                    ChangePasswordForm(
                        onChangePassword = { password, reEncrypt ->
                            newPassword = password
                            shouldReEncrypt = reEncrypt
                            showConfirmPasswordChangeDialog = true
                        },
                        inputValidator = inputValidator,
                        passwordEvaluator = passwordEvaluator
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Two-Factor Authentication",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                    if (is2FAEnabled) {
                        Button(onClick = onDisable2FA) {
                            Text("Disable")
                        }
                    } else {
                        Button(onClick = onEnable2FA) {
                            Text("Enable")
                        }
                    }
                }
            }
        }
        item {
            ExpandableSectionItem(
                title = "Dangerous",
                onExpand = { isDangerousExpanded = !isDangerousExpanded }
            )
            AnimatedVisibility(
                visible = isDangerousExpanded
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
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
    }
}

@Composable
@Preview(showBackground = true)
fun AccountFormPreview() {
    AccountForm(
        accountInfo = AccountInfo(
            firstName = "John",
            lastName = "Doe",
            email = "william.henry.harrison@example-pet-store.com"
        ),
        is2FAEnabled = false,
        onSaveInfo = { _, _, _ -> },
        onChangePassword = { _, _ -> },
        onEnable2FA = {},
        onDisable2FA = {},
        onDeleteAccount = {},
        inputValidator = DebugValidator(),
        passwordEvaluator = PasswordEvaluator()
    )
}