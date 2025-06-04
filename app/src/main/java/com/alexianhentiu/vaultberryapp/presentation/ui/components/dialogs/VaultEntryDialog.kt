package com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun VaultEntryDialog(
    formTitle: String,
    initialEntry: DecryptedVaultEntry? = null,
    onDismissRequest: () -> Unit,
    onSubmit: (DecryptedVaultEntry) -> Unit,
    validator: (InputType) -> (String) -> Boolean = { { true } },
    evaluatePasswordStrength: (String) -> PasswordStrength = { PasswordStrength.NONE }
) {
    var title by remember { mutableStateOf(initialEntry?.title ?: "") }
    var url by remember { mutableStateOf(initialEntry?.url ?: "") }
    var username by remember { mutableStateOf(initialEntry?.username ?: "") }
    var password by remember { mutableStateOf(initialEntry?.password ?: "") }
    var notes by remember { mutableStateOf(initialEntry?.notes ?: "") }

    var isTitleValid by remember { mutableStateOf(validator(InputType.ENTRY_TITLE)(title)) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = formTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )

                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.entry_title_label),
                            initialText = title,
                            onInputChange = { newTitle, valid ->
                                title = newTitle
                                isTitleValid = valid
                            },
                            validate = validator(InputType.ENTRY_TITLE)
                        )
                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.entry_url_label),
                            initialText = url,
                            onInputChange = { newUrl, _ ->
                                url = newUrl
                            }
                        )
                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.entry_username_label),
                            initialText = username,
                            onInputChange = { newUsername, _ ->
                                username = newUsername
                            }
                        )
                        PasswordField(
                            onPasswordChange = { newPassword, _ ->
                                password = newPassword
                            },
                            initialText = password,
                            evaluateStrength = evaluatePasswordStrength,
                            showStrengthIndicator = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.entry_notes_label),
                            initialText = notes,
                            onInputChange = { newNotes, _ ->
                                notes = newNotes
                            }
                        )

                        Row {
                            OutlinedButton(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(16.dp),
                                onClick = onDismissRequest
                            ) {
                                Text(stringResource(R.string.cancel_button_text))
                            }
                            Button(
                                enabled = isTitleValid,
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(16.dp),
                                onClick = {
                                    val entry = DecryptedVaultEntry(
                                        title = title,
                                        url = url,
                                        username = username,
                                        password = password,
                                        notes = notes
                                    )
                                    onSubmit(entry)
                                }
                            ) {
                                Text(stringResource(R.string.confirm_button_text))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEntryDialogPreview() {
    VaultEntryDialog(
        formTitle = "Add new entry",
        onDismissRequest = {},
        onSubmit = {}
    )
}