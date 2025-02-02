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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.model.Account

@Composable
fun ChangeAccountInfoForm(
    account: Account,
    onSaveInfo: (String?, String?, String?) -> Unit, // Pass null for unchanged values
) {
    var firstName by remember { mutableStateOf(account.firstName) }
    var lastName by remember { mutableStateOf(account.lastName) }
    var email by remember { mutableStateOf(account.email) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(8.dp)
    ) {
        Row {
            OutlinedTextField(
                value = firstName ?: "",
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.weight(0.49f)
            )
            Spacer(modifier = Modifier.weight(0.02f))
            OutlinedTextField(
                value = lastName ?: "",
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.weight(0.49f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onSaveInfo(
                    if (firstName?.isBlank() == true || firstName == account.firstName)
                        null else firstName,
                    if (lastName?.isBlank() == true || lastName == account.lastName)
                        null else lastName,
                    if (email.isBlank() || email == account.email) null else email
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Changes")
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ChangeAccountInfoFormPreview() {
    ChangeAccountInfoForm(
        account = Account(
            firstName = "John",
            lastName = "Doe",
            email = "john.c.breckinridge@altostrat.com"
        ),
        onSaveInfo = { _, _, _ -> },
    )
}