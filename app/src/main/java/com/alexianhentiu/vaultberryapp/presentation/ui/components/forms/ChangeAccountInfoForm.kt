package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo

@Composable
fun ChangeAccountInfoForm(
    accountInfo: AccountInfo,
    onSaveInfo: (String?, String?, String?) -> Unit, // Pass null for unchanged values
) {
    var firstName by remember { mutableStateOf(accountInfo.firstName) }
    var lastName by remember { mutableStateOf(accountInfo.lastName) }
    var email by remember { mutableStateOf(accountInfo.email) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(8.dp)
    ) {
        Row {
            OutlinedTextField(
                value = firstName ?: "",
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.first_name_label)) },
                modifier = Modifier.weight(0.49f)
            )
            Spacer(modifier = Modifier.weight(0.02f))
            OutlinedTextField(
                value = lastName ?: "",
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.last_name_label)) },
                modifier = Modifier.weight(0.49f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onSaveInfo(
                    if (email.isBlank() || email == accountInfo.email) null else email,
                    if (firstName?.isBlank() == true || firstName == accountInfo.firstName)
                        null else firstName,
                    if (lastName?.isBlank() == true || lastName == accountInfo.lastName)
                        null else lastName
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.save_button_text))
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ChangeAccountInfoFormPreview() {
    ChangeAccountInfoForm(
        accountInfo = AccountInfo(
            firstName = "John",
            lastName = "Doe",
            email = "john.c.breckinridge@altostrat.com"
        ),
        onSaveInfo = { _, _, _ -> },
    )
}