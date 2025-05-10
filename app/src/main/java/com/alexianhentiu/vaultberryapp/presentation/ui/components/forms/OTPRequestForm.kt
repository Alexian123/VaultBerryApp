package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.utils.types.ValidatedFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun OTPRequestForm(
    onContinueClicked: (String) -> Unit,
    onCancelClicked: () -> Unit,
    validator: (ValidatedFieldType) -> (String) -> Boolean = { { true } },
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "A one-time password will be sent to your email.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Left
        )
        ValidatedTextField(
            label = "Email",
            onInputChange = { newEmail, valid ->
                email = newEmail
                isEmailValid = valid
                },
            isValid = validator(ValidatedFieldType.EMAIL),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            OutlinedButton(
                onClick = { onCancelClicked() },
                modifier = Modifier.weight(0.4f)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                onClick = { onContinueClicked(email) },
                modifier = Modifier.weight(0.4f),
                enabled = isEmailValid
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OTPRequestFormPreview() {
    OTPRequestForm(
        onContinueClicked = {},
        onCancelClicked = {}
    )
}
