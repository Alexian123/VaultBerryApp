package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.BoundedNumberField

@Composable
fun ApiConfigForm(
    apiUrl: String,
    apiCertificate: ByteArray?,
    isPinging: Boolean = false,
    onSaveUrlClicked: (String) -> Unit,
    onTestConnectionClicked: (String, Int) -> Unit,
    onImportCertClicked: () -> Unit,
    onClearCertClicked: () -> Unit,
    onExitClicked: () -> Unit
) {
    val byteLowerBound = 0
    val byteUpperBound = 255

    val ipBytes by remember { mutableStateOf(IntArray(4)) }
    var port by remember { mutableIntStateOf(0) }

    var showConfirmClearCertDialog by remember { mutableStateOf(false) }

    fun generateIP(): String {
        return ipBytes.joinToString(".")
    }

    fun parseUrl(url: String) {
        val ipRegex = """\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}""".toRegex()
        val ipMatch = ipRegex.find(url)
        if (ipMatch != null) {
            val ip = ipMatch.value.split(".").map { it.toInt() }
            ipBytes[0] = ip[0]
            ipBytes[1] = ip[1]
            ipBytes[2] = ip[2]
            ipBytes[3] = ip[3]
        }

        val portRegex = """:\d{1,5}""".toRegex()
        val portMatch = portRegex.find(url)
        if (portMatch != null) {
            port = portMatch.value.substring(1).toInt()
        }
    }

    LaunchedEffect(apiUrl) {
        parseUrl(apiUrl)
    }

    if (showConfirmClearCertDialog) {
        ConfirmActionDialog(
            title = stringResource(R.string.clear_cert_dialog_title),
            message = stringResource(R.string.clear_cert_dialog_message),
            onSubmit = {
                onClearCertClicked()
                showConfirmClearCertDialog = false
            },
            onDismissRequest = { showConfirmClearCertDialog = false }
        )
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.ip_and_port_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BoundedNumberField(
                value = ipBytes[0],
                onValueChange = { ipBytes[0] = it },
                label = stringResource(R.string.byte_4_label),
                lowerBound = byteLowerBound,
                upperBound = byteUpperBound,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(8.dp)
            )

            Text(text = ".", fontWeight = FontWeight.Bold)

            BoundedNumberField(
                value = ipBytes[1],
                onValueChange = { ipBytes[1] = it },
                label = stringResource(R.string.byte_3_label),
                lowerBound = byteLowerBound,
                upperBound = byteUpperBound,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(8.dp)
            )

            Text(text = ".", fontWeight = FontWeight.Bold)

            BoundedNumberField(
                value = ipBytes[2],
                onValueChange = { ipBytes[2] = it },
                label = stringResource(R.string.byte_2_label),
                lowerBound = byteLowerBound,
                upperBound = byteUpperBound,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(8.dp)
            )

            Text(text = ".", fontWeight = FontWeight.Bold)

            BoundedNumberField(
                value = ipBytes[3],
                onValueChange = { ipBytes[3] = it },
                label = stringResource(R.string.byte_1_label),
                lowerBound = byteLowerBound,
                upperBound = byteUpperBound,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BoundedNumberField(
                value = port,
                onValueChange = { port = it },
                label = stringResource(R.string.port_label),
                lowerBound = 0,
                upperBound = 99999,
                modifier = Modifier
                    .weight(0.4f)
                    .padding(8.dp)
            )

            OutlinedButton(
                onClick = {
                    onTestConnectionClicked(generateIP(), port)
                },
                enabled = !isPinging,
                modifier = Modifier
                    .weight(0.6f)
                    .padding(start = 16.dp)
            ) {
                AnimatedVisibility(visible = isPinging, enter = fadeIn(), exit = fadeOut()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
                AnimatedVisibility(visible = !isPinging, enter = fadeIn(), exit = fadeOut()) {
                    Text(text = stringResource(R.string.test_connection_button_text))
                }
            }
        }

        Button(
            onClick = {
                val ip = generateIP()
                onSaveUrlClicked("https://$ip:$port/")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Save")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 32.dp))

        Text(
            text = stringResource(R.string.ssl_cert_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (apiCertificate != null) {
                Text(
                    text = stringResource(R.string.status_loaded),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    imageVector = Icons.Filled.CheckCircleOutline,
                    contentDescription = null,
                )
            } else {
                Text(
                    text = stringResource(R.string.status_missing),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    showConfirmClearCertDialog = true
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(stringResource(R.string.clear_cert_button_text))
            }
            Button(
                onClick = onImportCertClicked,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(stringResource(R.string.import_cert_button_text))
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 32.dp))

        Text(
            text = stringResource(R.string.restart_required),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = onExitClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.exit_button_text))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ApiConfigFormPreview() {
    ApiConfigForm(
        apiUrl = "",
        apiCertificate = null,
        onSaveUrlClicked = {},
        onTestConnectionClicked = {_, _ -> },
        onImportCertClicked = {},
        onExitClicked = {},
        onClearCertClicked = {}
    )
}