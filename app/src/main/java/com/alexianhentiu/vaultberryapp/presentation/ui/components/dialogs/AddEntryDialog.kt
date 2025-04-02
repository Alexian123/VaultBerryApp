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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun AddEntryDialog(
    formTitle: String,
    onDismissRequest: () -> Unit,
    onSubmit: (DecryptedVaultEntry) -> Unit,
    inputValidator: InputValidator
) {
    var title by remember { mutableStateOf( "") }
    var url by remember { mutableStateOf( "") }
    var username by remember { mutableStateOf( "") }
    var password by remember { mutableStateOf( "") }
    var notes by remember { mutableStateOf( "") }

    var isTitleValid by remember { mutableStateOf(false) }

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
                            label = "Title",
                            onInputChange = { newTitle, valid ->
                                title = newTitle
                                isTitleValid = valid
                            },
                            isValid = inputValidator::validateEntryTitle
                        )
                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = "URL",
                            onInputChange = { newUrl, _ ->
                                url = newUrl
                            }
                        )
                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Username",
                            onInputChange = { newUsername, _ ->
                                username = newUsername
                            }
                        )
                        PasswordField(
                            onPasswordChange = { newPassword, _ ->
                                password = newPassword
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        ValidatedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Notes",
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
                                Text("Cancel")
                            }
                            Button(
                                enabled = isTitleValid,
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(16.dp),
                                onClick = {
                                    val entry = DecryptedVaultEntry(
                                        timestamp = System.currentTimeMillis(),
                                        title = title,
                                        url = url,
                                        username = username,
                                        password = password,
                                        notes = notes
                                    )
                                    onSubmit(entry)
                                }
                            ) {
                                Text("Confirm")
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
    AddEntryDialog(
        formTitle = "Add new entry",
        onDismissRequest = {},
        onSubmit = {},
        inputValidator = InputValidator()
    )
}