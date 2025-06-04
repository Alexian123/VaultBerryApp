package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
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
    validator: (InputType) -> (String) -> Boolean = { { true } },
    evaluatePasswordStrength: (String) -> PasswordStrength = { PasswordStrength.NONE },
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
            title = stringResource(R.string.delete_account_title),
            message = stringResource(R.string.delete_account_message),
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
            title = stringResource(R.string.change_password_title),
            message = stringResource(R.string.change_password_message),
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
                title = stringResource(R.string.account_info_title),
                onExpand = { isInfoExpanded = !isInfoExpanded },
                imageVector = Icons.Filled.AccountBox
            )
            AnimatedVisibility(visible = isInfoExpanded) {
                ChangeAccountInfoForm(
                    accountInfo = accountInfo,
                    onSaveInfo = onSaveInfo,
                    validator = validator
                )
            }
        }
        item {
            ExpandableSectionItem(
                title = stringResource(R.string.account_security_title),
                onExpand = { isSecurityExpanded = !isSecurityExpanded },
                imageVector = Icons.Filled.Security
            )
            AnimatedVisibility(visible = isSecurityExpanded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.password_label),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                    ChangePasswordForm(
                        onChangePassword = { password, reEncrypt ->
                            newPassword = password
                            shouldReEncrypt = reEncrypt
                            showConfirmPasswordChangeDialog = true
                        },
                        validator = validator,
                        evaluatePasswordStrength = evaluatePasswordStrength
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = stringResource(R.string.two_factor_auth_label),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                    if (is2FAEnabled) {
                        Button(onClick = onDisable2FA) {
                            Text(stringResource(R.string.disable_button_text))
                        }
                    } else {
                        Button(onClick = onEnable2FA) {
                            Text(stringResource(R.string.enable_button_text))
                        }
                    }
                }
            }
        }
        item {
            ExpandableSectionItem(
                title = stringResource(R.string.account_danger_title),
                onExpand = { isDangerousExpanded = !isDangerousExpanded },
                imageVector = Icons.Filled.Dangerous
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
                        Text(stringResource(R.string.delete_account_button_text))
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
        onDeleteAccount = {}
    )
}