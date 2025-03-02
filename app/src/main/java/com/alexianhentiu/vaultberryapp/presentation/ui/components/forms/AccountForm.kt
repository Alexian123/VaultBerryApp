package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
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
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.ExpandableSectionItem

@Composable
fun AccountForm(
    account: Account,
    onSaveInfo: (String?, String?, String?) -> Unit, // Pass null for unchanged values
    onChangePassword: (String) -> Unit,
    onDeleteAccount: () -> Unit,
    inputValidator: InputValidator
) {
    var isInfoExpanded by remember { mutableStateOf(false) }
    var isSecurityExpanded by remember { mutableStateOf(false) }
    var isDangerousExpanded by remember { mutableStateOf(false) }

    Column {
        ExpandableSectionItem(
            title = "Account Info",
            onExpand = { isInfoExpanded = !isInfoExpanded }
        )
        AnimatedVisibility(visible = isInfoExpanded) {
            ChangeAccountInfoForm(
                account = account,
                onSaveInfo = onSaveInfo
            )
        }
        ExpandableSectionItem(
            title = "Security",
            onExpand = { isSecurityExpanded = !isSecurityExpanded }
        )
        AnimatedVisibility(visible = isSecurityExpanded) {
            ChangePasswordForm(
                onChangePassword = onChangePassword,
                inputValidator = inputValidator
            )
        }
        ExpandableSectionItem(
            title = "Dangerous",
            onExpand = { isDangerousExpanded = !isDangerousExpanded }
        )
        AnimatedVisibility(
            visible = isDangerousExpanded,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = onDeleteAccount
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
        onSaveInfo = { _, _, _ -> },
        onChangePassword = {},
        onDeleteAccount = {},
        inputValidator = InputValidator()
    )
}