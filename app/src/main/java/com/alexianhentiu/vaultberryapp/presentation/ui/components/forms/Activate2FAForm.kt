package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun Activate2FAForm(
    secretKey: String,
    qrBitmap: ImageBitmap? = null,
    onActivate2FA: (String) -> Unit,
    onCopyClicked: (String) -> Unit,
    validator: (InputType) -> (String) -> Boolean = { { true } }
) {
    var code by remember { mutableStateOf("") }
    var isCodeValid by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        qrBitmap?.let {
            Text(
                text = stringResource(R.string.activate_2fa_qr_code_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Image(
                bitmap = it,
                contentDescription = stringResource(R.string.activate_2fa_qr_code_image_description),
                modifier = Modifier.size(200.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            text = stringResource(R.string.activate_2fa_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ValidatedTextField(
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = stringResource(R.string.secret_totp_key_label),
            initialText = secretKey,
            showCopyToClipboardButton = true,
            onCopyClicked = onCopyClicked,
            textFieldType = TextFieldType.OUTLINED
        )
        Text(
            text = stringResource(R.string.verify_2fa_code_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ValidatedTextField(
            label = stringResource(R.string.mfa_code_label),
            onInputChange = { newCode, valid ->
                code = newCode
                isCodeValid = valid
            },
            isValid = isCodeValid,
            validate = validator(InputType.MFA_CODE),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onActivate2FA(code) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = isCodeValid
        ) {
            Text(stringResource(R.string.verify_button_text))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Activate2FAFormPreview() {
    Activate2FAForm(
        secretKey = "ABCDEFGHIJKLMNOP",
        qrBitmap = ImageBitmap(width = 200, height = 200) ,
        onActivate2FA = {},
        onCopyClicked = {}
    )
}