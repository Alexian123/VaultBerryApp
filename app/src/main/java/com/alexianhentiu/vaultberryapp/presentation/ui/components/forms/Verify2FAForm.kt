package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ValidatedFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun Verify2FAForm(
    onContinueClicked: (String) -> Unit,
    onCancelClicked: () -> Unit,
    validator: (ValidatedFieldType) -> (String) -> Boolean = { { true } }
) {
    var code by remember { mutableStateOf("") }
    var isCodeValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ValidatedTextField(
            label = stringResource(R.string.mfa_code_label),
            onInputChange = { newCode, valid ->
                code = newCode
                isCodeValid = valid
            },
            isValid = isCodeValid,
            validate = validator(ValidatedFieldType.MFA_CODE),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onContinueClicked(code) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isCodeValid
        ) {
            Text(stringResource(R.string.continue_button_text))
        }
        TextButton(
            onClick = { onCancelClicked() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.cancel_button_text))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Verify2FAFormPreview() {
    Verify2FAForm(
        onContinueClicked = {},
        onCancelClicked = {}
    )
}